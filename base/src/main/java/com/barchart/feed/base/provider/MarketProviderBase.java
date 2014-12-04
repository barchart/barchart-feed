package com.barchart.feed.base.provider;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection.Monitor;
import com.barchart.feed.api.connection.Subscription;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.consumer.MetadataService;
import com.barchart.feed.api.filter.Filter;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.Metadata.MetaType;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.MetadataID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.participant.FrameworkAgent;
import com.barchart.feed.base.participant.FrameworkAgentLifecycleHandler;
import com.barchart.feed.base.provider.MarketDataGetters.MDGetter;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.sub.SubCommand;
import com.barchart.feed.base.sub.SubCommand.Type;
import com.barchart.feed.base.sub.SubscriptionHandler;
import com.barchart.feed.base.sub.SubscriptionType;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.inst.Exchanges;
import com.barchart.util.common.collections.strict.StrictConcurrentHashMap;
import com.barchart.util.value.api.Fraction;

public abstract class MarketProviderBase<Message extends MarketMessage>
		implements MarketService, MarketMakerProvider<Message>,
		FrameworkAgentLifecycleHandler {

	protected static final Logger log = LoggerFactory.getLogger(
			MarketProviderBase.class);

	protected final MarketFactory factory;
	protected final MetadataService metaService;
	protected final SubscriptionHandler subHandler;

	protected final ConcurrentMap<InstrumentID, MarketDo> marketMap =
			new ConcurrentHashMap<InstrumentID, MarketDo>();

	protected final ConcurrentMap<String, InstrumentID> symbolMap =
			new ConcurrentHashMap<String, InstrumentID>();

	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agents =
			new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();

	private final ConcurrentMap<InstrumentID, Subscription<Instrument>> defSubs =
			new ConcurrentHashMap<InstrumentID, Subscription<Instrument>>();

	private final ConcurrentMap<InstrumentID, VarSubscription> varSubs =
			new ConcurrentHashMap<InstrumentID, VarSubscription>();

	// Not implemented
	private final ConcurrentMap<ExchangeID, Subscription<Exchange>> exchSubs =
			new ConcurrentHashMap<ExchangeID, Subscription<Exchange>>();

	protected MarketProviderBase(final MarketFactory factory, 
			final MetadataService metaService,
			final SubscriptionHandler handler) {
		
		this.factory = factory;
		this.metaService = metaService;
		subHandler = handler;
	}

	/* ***** ***** Consumer Agent ***** ***** */

	@Override
	public <V extends MarketData<V>> ConsumerAgent register(final MarketObserver<V> callback,
			final Class<V> clazz) {

		final MDGetter<V> getter = MarketDataGetters.get(clazz);

		if (getter == null) {
			throw new IllegalArgumentException("Illegal class type " + clazz.getName());
		}

		final FrameworkAgent<V> agent = new BaseAgent<V>(this, clazz, getter, callback);

		attachAgent(agent);

		return agent.agent();
	}

	protected class BaseAgent<V extends MarketData<V>> implements FrameworkAgent<V>, Agent {

		private final Class<V> clazz;
		private final MDGetter<V> getter;
		private final FrameworkAgentLifecycleHandler agentHandler;
		private final MarketObserver<V> callback;

		private volatile State state = State.ACTIVATED;

		private final Set<Exchange> incExchanges = new HashSet<Exchange>();
		private final Set<Exchange> exExchanges = new HashSet<Exchange>();

		private final Set<Instrument> incInsts = new HashSet<Instrument>();
		private final Set<Instrument> exInsts = new HashSet<Instrument>();

		private final Set<String> incUnknown = new HashSet<String>();
		private final Set<String> exUnknown = new HashSet<String>();

		private Filter filter = new DefaultFilter();

		BaseAgent(
				final FrameworkAgentLifecycleHandler agentHandler,
				final Class<V> clazz, 
				final MDGetter<V> getter,
				final MarketObserver<V> callback) {
			
			if(agentHandler == null) {
				throw new IllegalArgumentException("Agent Handler cannot be null");
			}
			if(clazz == null) {
				throw new IllegalArgumentException("Class cannot be null");
			}
			if(getter == null) {
				throw new IllegalArgumentException("Market data getter cannot be null");
			}
			if(callback == null) {
				throw new IllegalArgumentException("Callback cannot be null");
			}
			
			this.agentHandler = agentHandler;
			this.clazz = clazz;
			this.getter = getter;
			this.callback = callback;

		}

		@Override
		public AgentType agentType() {
			if(clazz == Market.class) {
				return AgentType.MARKET;
			}
			if(clazz == Trade.class) {
				return AgentType.TRADE;
			}
			if(clazz == Book.class) {
				return AgentType.BOOK;
			}
			if(clazz == Session.class) {
				return AgentType.SESSION;
			}
			if(clazz == Cuvol.class) {
				return AgentType.CUVOL;
			}
			throw new IllegalStateException("Unknown Agent Type");
		}

		@Override
		public Agent agent() {
			return this;
		}

		/* ***** ***** Framework Methods ***** ***** */

		@Override
		public Class<V> type() {
			return clazz;
		}

		@Override
		public MarketObserver<V> callback() {
			return callback;
		}

		@Override
		public V data(final Market market) {
			/* getter calling copy() */
			return getter.get(market);
		}

		@Override
		public Set<String> interests() {

			final Set<String> interests = new HashSet<String>();

			for (final Exchange e : incExchanges) {
				interests.add(e.id().toString());
			}

			for (final Instrument i : incInsts) {
				
				String symbol = i.symbol();
				if(symbol.contains("|")) {
					symbol = i.vendorSymbols().get(VendorID.BARCHART_SHORT);
				}
				
				interests.add(symbol);
			}

			return interests;
		}

		/* ***** ***** Filter Methods ***** ***** */

		@Override
		public boolean hasMatch(final Instrument instrument) {
			return filter.hasMatch(instrument);
		}

		/*
		 * Allow for filter to be overridden.
		 */
		private class DefaultFilter implements Filter {

			@SuppressWarnings("deprecation")
			@Override
			public boolean hasMatch(final Instrument instrument) {
				/* Work bottom up on the hierarchy */

				if(incUnknown.contains(instrument.symbol())) {
					return true;
				}

				if(exUnknown.contains(instrument.symbol())) {
					return false;
				}

				if (incInsts.contains(instrument)) {
					return true;
				}

				if (exInsts.contains(instrument)) {
					return false;
				}

				if (instrument.exchange().isNull()) {
					log.debug("Exchange is NULL for " + instrument.symbol() + " "
							+ instrument.exchangeCode());
					return false;
				}

				if (incExchanges.contains(instrument.exchange())) {
					return true;
				}

				if (exExchanges.contains(instrument.exchange())) {
					return false;
				}

				return false;
			}

			@Override
			public String expression() {
				throw new UnsupportedOperationException();
			}

		}

		@Override
		public String expression() {
			throw new UnsupportedOperationException();
		}

		/* ***** ***** Consumer Methods ***** ***** */

		@Override
		public State state() {
			return state;
		}

		@Override
		public boolean isActive() {
			return state == State.ACTIVATED;
		}

		@Override
		public void activate() {

			if(state == State.TERMINATED) {
				return;
			}

			state = State.ACTIVATED;
		}

		@Override
		public void deactivate() {

			if(state == State.TERMINATED) {
				return;
			}

			state = State.DEACTIVATED;
		}

		@Override
		public synchronized void terminate() {

			/* Unsubscribe to all */
			subHandler.unsubscribe(unsubscribeAll(this));

			state = State.TERMINATED;
			agentHandler.detachAgent(this);

		}

		@Override
		public synchronized Observable<Result<Instrument>> include(final String... symbols) {
			
			return metaService.instrument(symbols)
					
				.flatMap(new Func1<Result<Instrument>, Observable<Result<Instrument>>>() {

					@Override
					public Observable<Result<Instrument>> call(final Result<Instrument> result) {
						
						final Map<String, List<Instrument>> instMap = result.results();
						final Map<String, Type> newInterests = new HashMap<String, Type>();

						for (final Entry<String, List<Instrument>> e : instMap.entrySet()) {

							/* Currently assuming we're only getting one inst back */
							final Instrument i = e.getValue().get(0);

							if (!i.isNull()) {
								
								/* Try to fire a snapshot if instrument is not already included*/
								if(!incInsts.contains(i)) {
									fireSnapshot(i.id());
								}

								exInsts.remove(i);
								incInsts.add(i);

								/* We have to use an alternate symbol for options */
								final String symbol = i.symbol();
								if(symbol.contains("|")) {
									newInterests.put(i.vendorSymbols().get(VendorID.BARCHART_SHORT), Type.INSTRUMENT);
								} else {
									newInterests.put(formatForJERQ(i.symbol()), Type.INSTRUMENT);
								}

							} else {
								/* For all failed lookups, store symbol and attempt to match
								 * in the hasMatch method. */
								incUnknown.add(e.getKey().toString());
								exUnknown.remove(e.getKey().toString());
							}

						}

						agentHandler.updateAgent(BaseAgent.this);

						final Set<SubCommand> newSubs = subscribe(BaseAgent.this, newInterests);
						if (!newSubs.isEmpty()) {
							log.debug("Sending new subs to sub handler");
							subHandler.subscribe(newSubs);
						}

						return Observable.just(result);
					}

				}

			);

		}
		
		@SuppressWarnings("unchecked")
		private void fireSnapshot(final InstrumentID id) {
			
			if(!marketMap.containsKey(id)) {
				log.warn("InstID {} not in market map", id);
				return;
			}
			
			final Market market = marketMap.get(id);
			
			if(market == null) {
				log.error("Should not happen");
				return;
			}
			
			final MarketData<V> data = getter.get(market);
			
			if(data.isNull()) {
				return;
			}
			
			callback.onNext((V) data);
			
		}

		@Override
		public synchronized Observable<Result<Instrument>> exclude(final String... symbols) {

			return metaService.instrument(symbols).flatMap(

				new Func1<Result<Instrument>, Observable<Result<Instrument>>>() {

					@Override
					public Observable<Result<Instrument>> call(final Result<Instrument> result) {

						final Map<String, List<Instrument>> instMap = result.results();
						final Map<String, Type> oldInterests = new HashMap<String, Type>();

						for (final Entry<String, List<Instrument>> e : instMap.entrySet()) {

							/* Currently assuming we're only getting one inst back */
							final Instrument i = e.getValue().get(0);

							if (!i.isNull()) {

								incInsts.remove(i);
								exInsts.add(i);

								/* We have to use an alternate symbol for options
								 * ...rolls eyes...
								 */
								final String symbol = i.symbol();
								if(symbol.contains("|")) {
									oldInterests.put(i.vendorSymbols().get(VendorID.BARCHART_SHORT), Type.INSTRUMENT);
								} else {
									oldInterests.put(formatForJERQ(i.symbol()), Type.INSTRUMENT);
								}

							} else {
								/*
								 * For all failed lookups, store symbol and attempt to match
								 * in the hasMatch method.
								 */
								incUnknown.remove(e.getKey().toString());
								exUnknown.add(e.getKey().toString());
							}

						}

						agentHandler.updateAgent(BaseAgent.this);

						final Set<SubCommand> oldSubs = unsubscribe(BaseAgent.this, oldInterests);
						if (!oldSubs.isEmpty()) {
							log.debug("Sending new unsubs to sub handler");
							subHandler.unsubscribe(oldSubs);
						}

						return Observable.just(result);
					}

				}

			);

		}

		/* ***** ***** Filter Updatable ***** ***** */

		@Override
		public void filter(final Filter filter) {
			this.filter = filter;
		}

		@Override
		public Filter filter() {
			return filter;
		}

		@Override
		@Deprecated
		public synchronized void include(final Metadata... meta) {

			final Map<String, Type> newInterests = new HashMap<String, Type>();

			for(final Metadata m : meta) {

				if(m == null || m.isNull()) {
					continue;
				}

				switch(m.type()) {

				default:
					// Ignore
					continue;
				case INSTRUMENT:

					final Instrument i = (Instrument)m;

					incInsts.add(i);
					exInsts.remove(i);

					/* We have to use an alternate symbol for options */
					final String symbol = i.symbol();
					if(symbol.contains("|")) {
						newInterests.put(i.vendorSymbols().get(VendorID.BARCHART_SHORT), Type.INSTRUMENT);
					} else {
						newInterests.put(formatForJERQ(i.symbol()), Type.INSTRUMENT);
					}

					continue;
				case EXCHANGE:
					incExchanges.add((Exchange)m);
					exExchanges.remove(m);
					newInterests.put(((Exchange)m).id().toString(), Type.EXCHANGE);
				}

			}

			agentHandler.updateAgent(this);

			final Set<SubCommand> newSubs = subscribe(this, newInterests);
			if (!newSubs.isEmpty()) {
				log.debug("Sending new subs to sub handler");
				subHandler.subscribe(newSubs);
			}

		}

		@Override
		public synchronized void include(final MetadataID<?>... metaIDs) {

			final List<InstrumentID> instIDs = new ArrayList<InstrumentID>();
			final List<Metadata> metas = new ArrayList<Metadata>();

			for(final MetadataID<?> m : metaIDs) {
				
				switch(m.metaType()) {
				default:
					throw new IllegalArgumentException("Unsupported Metadata Type " + m.metaType());
				case INSTRUMENT:
					instIDs.add((InstrumentID) m);
					break;
				case EXCHANGE:
					final Exchange e = Exchanges.fromID((ExchangeID)m);
					if(e.isNull()) {
						log.warn("Attempted to include invalid exchange ID " + ((ExchangeID)m).id());
					} else {
						metas.add(e);
					}
					break;
				}

			}

			final Map<InstrumentID, Instrument> iMap = metaService.instrument(
					instIDs.toArray(new InstrumentID[0])).toBlockingObservable().single();

			for(final Entry<InstrumentID, Instrument> e : iMap.entrySet()) {
				if(!e.getValue().isNull()) {
					metas.add(e.getValue());
				}
			}

			include(metas.toArray(new Metadata[0]));
		}

		@Override
		@Deprecated
		public synchronized void exclude(final Metadata... metas) {

			final Map<String, Type> oldInterests = new HashMap<String, Type>();

			for(final Metadata m : metas) {

				if(m == null || m.isNull()) {
					continue;
				}

				switch(m.type()) {

				default:
					// Ignore
					continue;
				case INSTRUMENT:

					final Instrument i = (Instrument)m;

					exInsts.add(i);
					incInsts.remove(i);

					/* We have to use an alternate symbol for options */
					final String symbol = i.symbol();
					if(symbol.contains("|")) {
						oldInterests.put(i.vendorSymbols().get(VendorID.BARCHART_SHORT), Type.INSTRUMENT);
					} else {
						oldInterests.put(formatForJERQ(i.symbol()), Type.INSTRUMENT);
					}

					continue;
				case EXCHANGE:
					exExchanges.add((Exchange)m);
					incExchanges.remove(m);
					oldInterests.put(((Exchange)m).id().toString(), Type.EXCHANGE);
				}

			}

			agentHandler.updateAgent(this);

			final Set<SubCommand> oldSubs = unsubscribe(this, oldInterests);
			if (!oldSubs.isEmpty()) {
				log.debug("Sending new unsubs to sub handler");
				subHandler.unsubscribe(oldSubs);
			}

		}

		@Override
		public synchronized void exclude(final MetadataID<?>... metaID) {

			final List<InstrumentID> ids = new ArrayList<InstrumentID>();

			for(final MetadataID<?> m : metaID) {

				if(m.metaType() == MetaType.INSTRUMENT) {
					ids.add((InstrumentID) m);
				}

			}

			final Map<InstrumentID, Instrument> iMap = metaService.instrument(
					ids.toArray(new InstrumentID[0])).toBlockingObservable().single();

			final List<Instrument> insts = new ArrayList<Instrument>();

			for(final Entry<InstrumentID, Instrument> e : iMap.entrySet()) {
				if(!e.getValue().isNull()) {
					insts.add(e.getValue());
				}
			}

			exclude(insts.toArray(new Instrument[0]));

		}

		@Override
		public synchronized void clear() {

			/* Unsubscribe to all */
			final Set<SubCommand> subsToUnsub = unsubscribeAll(this);
			final StringBuilder sb = new StringBuilder().append("UNSUB ******** ");
			for(final SubCommand s : subsToUnsub) {
				sb.append(s.interest()).append(" ");
			}
			log.debug(sb.toString());

			subHandler.unsubscribe(unsubscribeAll(this));

			incInsts.clear();
			exInsts.clear();
			incExchanges.clear();
			exExchanges.clear();

			agentHandler.updateAgent(this);

		}

		@Override
		public void includeSymbol(final String... symbols) {
			include(symbols).toBlockingObservable().first();
		}

		@Override
		public void excludeSymbol(final String... symbols) {
			exclude(symbols).toBlockingObservable().first();
		}

	}  // END BASE AGENT


	/* ***** ***** Subscription Aggregation Methods ***** ***** */

	private final Map<String, List<FrameworkAgent<?>>> instToAgentsMap =
			new ConcurrentHashMap<String, List<FrameworkAgent<?>>>();

	private Set<SubscriptionType> aggregate(final String interest) {

		final Set<SubscriptionType> agg = EnumSet.noneOf(SubscriptionType.class);

		if (!instToAgentsMap.containsKey(interest)) {
			return agg;
		}

		for (final FrameworkAgent<?> agent : instToAgentsMap.get(interest)) {
			agg.addAll(SubscriptionType.mapMarketEvent(agent.type()));
		}

		return agg;
	}

	private SubCommand subscribe(
			final FrameworkAgent<?> agent, 
			final String symbol, 
			final Type type) {

		synchronized(instToAgentsMap) {

			final Set<SubscriptionType> newSubs = SubscriptionType.mapMarketEvent(agent.type());

			if (!instToAgentsMap.containsKey(symbol) && !newSubs.isEmpty()) {
				instToAgentsMap.put(symbol, new ArrayList<FrameworkAgent<?>>());
			}

			final Set<SubscriptionType> stuffToAdd = EnumSet.copyOf(newSubs);
			stuffToAdd.removeAll(aggregate(symbol));

			instToAgentsMap.get(symbol).add(agent);

			if (!stuffToAdd.isEmpty()) {
				return new SubBase(symbol, type, stuffToAdd);
			} else {
				return SubCommand.NULL;
			}

		}
	}

	private Set<SubCommand> subscribe(final FrameworkAgent<?> agent, final Map<String, Type> symbols) {

		final Set<SubCommand> newSubs = new HashSet<SubCommand>();

		for (final Entry<String, SubCommand.Type> e : symbols.entrySet()) {
			final SubCommand sub = subscribe(agent, e.getKey(), e.getValue());
			if (!sub.isNull()) {
				newSubs.add(sub);
			}
		}

		return newSubs;
	}

	private SubCommand unsubscribe(
			final FrameworkAgent<?> agent, 
			final String symbol, 
			final Type type) {

		synchronized(instToAgentsMap) {

			final Set<SubscriptionType> oldSubs = SubscriptionType.mapMarketEvent(agent.type());

			if(instToAgentsMap.containsKey(symbol)){
				instToAgentsMap.get(symbol).remove(agent);
				if(instToAgentsMap.get(symbol).isEmpty()) {
					instToAgentsMap.remove(symbol);
				}
			}

			final Set<SubscriptionType> stuffToRemove = EnumSet.copyOf(oldSubs);
			stuffToRemove.removeAll(aggregate(symbol));

			if (!stuffToRemove.isEmpty()) {
				return new SubBase(symbol, SubCommand.Type.INSTRUMENT, stuffToRemove);
			} else {
				return SubCommand.NULL;
			}
		}
	}

	private synchronized Set<SubCommand> unsubscribe(final FrameworkAgent<?> agent,
			final Map<String, Type> symbols) {

		final Set<SubCommand> newSubs = new HashSet<SubCommand>();

		for (final Entry<String,Type> interest : symbols.entrySet()) {
			final SubCommand sub = unsubscribe(agent, interest.getKey(), interest.getValue());
			if (!sub.isNull()) {
				newSubs.add(sub);
			}
		}

		return newSubs;
	}

	private Set<SubCommand> unsubscribeAll(final FrameworkAgent<?> agent) {
		final Map<String, Type> symMap = new HashMap<String, Type>();
		
		for(final String s : agent.interests()) {
			symMap.put(s, Type.INSTRUMENT);
		}
		
		return unsubscribe(agent, symMap);
	}

	class RefEqualsList<T> extends ArrayList<T> {

		private static final long serialVersionUID = -7398964176380704808L;

		@Override
		public boolean equals(final Object o) {
			if(this == o) {
				return true;
			} else {
				return false;
			}
		}
	}

	private static String formatForJERQ(final String symbol) {

		if (symbol == null) {
			return "";
		}

		if (symbol.length() < 3) {
			return symbol;
		}

		/* e.g. GOOG */
		if (!Character.isDigit(symbol.charAt(symbol.length() - 1))) {
			return symbol;
		}

		/* e.g. ESH2013 -> ESH3 */
		if (Character.isDigit(symbol.charAt(symbol.length() - 4))) {
			return new StringBuilder(symbol).delete(symbol.length() - 4,
					symbol.length() - 1).toString();
		}

		return symbol;
	}

	/* ***** ***** Agent Lifecycle Methods ***** ***** */
	@Override
	public void attachAgent(final FrameworkAgent<?> agent) {
		
		if (agents.containsKey(agent)) {
			
			updateAgent(agent);
			
		} else {

			agents.put(agent, new Boolean(false));

			for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
				e.getValue().attachAgent(agent);
			}

		}
	}

	@Override
	public void updateAgent(final FrameworkAgent<?> agent) {

		if (!agents.containsKey(agent)) {

			attachAgent(agent);

		} else {

			for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
				e.getValue().updateAgent(agent);
			}

		}

	}

	@Override
	public void detachAgent(final FrameworkAgent<?> agent) {

		if (!agents.containsKey(agent)) {
			return;
		}

		agents.remove(agent);

		for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
			e.getValue().detachAgent(agent);
		}

	}

	/* ***** ***** SubscriptionService ***** ***** */

	@Override
	public Map<InstrumentID, Subscription<Instrument>> instruments() {
		return defSubs;
	}

	@Override
	public Map<ExchangeID, Subscription<Exchange>> exchanges() {
		return exchSubs;
	}

	// ######################## // ########################
	
	private final ConcurrentMap<InstrumentID, PublishSubject<Market>> awaitingSnaps = 
			new StrictConcurrentHashMap<InstrumentID, PublishSubject<Market>>(InstrumentID.class);
	
	private final ConsumerAgent snapshotAgent = register(new MarketObserver<Market>() {

			@Override
			public void onNext(final Market v) {
				/* Does nothing */
			}
			
		}, Market.class);

	@Override
	public synchronized Observable<Market> snapshot(final InstrumentID instID) {

		if(marketMap.containsKey(instID)) {
			
			final Market market = marketMap.get(instID).freeze();
			return Observable.just(market);
			
		} else {
			
			if(!awaitingSnaps.containsKey(instID)) {
				
				final PublishSubject<Market> sub = PublishSubject.<Market> create();
				awaitingSnaps.putIfAbsent(instID, sub);
				
				snapshotAgent.include(instID);
				
			}
			
			return awaitingSnaps.get(instID);
		}

	}

	// ######################## // ########################

	@Override
	public Observable<Map<InstrumentID, Instrument>> instrument(final InstrumentID... ids) {
		return metaService.instrument(ids);
	}

	@Override
	public Observable<Result<Instrument>> instrument(final String... symbols) {
		return metaService.instrument(symbols);
	}

	@Override
	public Observable<Result<Instrument>> instrument(final SearchContext ctx, 
			final String... symbols) {
		return metaService.instrument(ctx, symbols);
	}

	// ######################## // ########################

	@Override
	public boolean register(final Instrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		MarketDo market = marketMap.get(instrument.id());

		final boolean wasAdded = (market == null);

		while (market == null) {
			market = factory.newMarket(instrument);
			market.setInstrument(instrument);
			marketMap.putIfAbsent(instrument.id(), market);
			market = marketMap.get(instrument.id());
		}

		if (wasAdded) {

			for (final FrameworkAgent<?> agent : agents.keySet()) {
				marketMap.get(instrument.id()).attachAgent(agent);
			}

			symbolMap.put(instrument.symbol(), instrument.id());

		}

		return wasAdded;
	}

	@Override
	public boolean unregister(final Instrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		final MarketDo market = marketMap.remove(instrument.id());

		final boolean wasRemoved = (market != null);

		if (wasRemoved) {

			for (final FrameworkAgent<?> agent : agents.keySet()) {
				marketMap.get(instrument.id()).detachAgent(agent);
			}

			symbolMap.remove(instrument.symbol());

		} else {
			log.warn("was not registered : {}", instrument);
		}

		return wasRemoved;
	}

	// ######################## // ########################

	@Override
	public int marketCount() {
		return marketMap.size();
	}

	@Override
	public boolean isRegistered(final Instrument instrument) {
		return marketMap.containsKey(instrument.id());
	}

	@Override
	public void clearAll() {
		marketMap.clear();
		symbolMap.clear();
	}

	// ######################## Make ########################
	
	@Override
	public void make(final Message message) {
		
		final Instrument instrument = message.getInstrument();

		if (!isValid(instrument)) {
			return;
		}
		
		MarketDo market = marketMap.get(instrument.id());
		
		final boolean valid = isValid(market);

		if(!valid) {
			register(instrument);
			market = marketMap.get(instrument.id());
		}
		
		market.runSafe(safeMake, message);  
		
		/* Check if any subject are awaiting snapshots 
		 * Check if session is null because first message for FUTURES will be CUVOL 
		 * and won't have snapshot info 
		 */
		if(!market.session().isNull()
				&& awaitingSnaps.containsKey(instrument.id())) {
			
			final PublishSubject<Market> sub = awaitingSnaps.remove(instrument.id());
			
			sub.onNext(market.freeze());
			sub.onCompleted();
			
		}

		/* Below is a hack to keep the subscriptions updated */
		/* If a new market is created, a new subscription is made,
		 * but it needs the State enum from market which should
		 * get set on the first market snapshot, which is why
		 * this comes after the above safeRun update of the message */

		/* If the state hasn't been set, this will mark it as Delayed,
		 * and we're not updating */
		Subscription.Lense lense;
		if(market.get(MarketField.STATE).contains(MarketStateEntry.IS_PUBLISH_REALTIME)) {
			lense = Subscription.Lense.REALTIME;
		} else if(market.get(MarketField.STATE).contains(MarketStateEntry.IS_PUBLISH_DELAYED)) {
			lense = Subscription.Lense.DELAYED;
		} else {
			lense = Subscription.Lense.NULL;
		}

		if(!valid) {
			varSubs.put(instrument.id(), new VarSubscription(instrument, lense));
			defSubs.put(instrument.id(), varSubs.get(instrument.id()));
		}

		varSubs.get(instrument.id()).setLense(lense);
		
	}

	private class VarSubscription implements Subscription<Instrument> {

		private final Instrument inst;
		private Subscription.Lense lense;

		VarSubscription(final Instrument inst, final Subscription.Lense lense) {
			this.inst = inst;
			this.lense = lense;
		}

		@Override
		public Lense lense() {
			return lense;
		}

		@Override
		public Instrument metadata() {
			return inst;
		}

		@Override
		public boolean isNull() {
			return false;
		}

		public void setLense(final Subscription.Lense lense) {
			this.lense = lense;
		}

	}

	protected MarketSafeRunner<Void, Message> safeMake = new MarketSafeRunner<Void, Message>() {

		@Override
		public Void runSafe(final MarketDo market, final Message message) {
			make(message, market);
			market.fireEvents();
			market.fireCallbacks();
			return null;
		}
	};

	protected abstract void make(Message message, MarketDo market);

	// ######################## Take ########################

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Instrument, V extends Value<V>> V take(final S instrument,
			final MarketField<V> field) {

		final MarketDo market = marketMap.get(instrument.id());

		if (market == null) {
			return MarketConst.NULL_MARKET.get(field).freeze();
		}

		return (V) market.runSafe(safeTake, field);
	}

	private final MarketSafeRunner<Value<?>, MarketField<?>> safeTake =
			new MarketSafeRunner<Value<?>, MarketField<?>>() {

		@Override
		public Value<?> runSafe(final MarketDo market,
				final MarketField<?> field) {
			return market.get(field).freeze();
		}
	};

	// ######################## // ########################

	protected boolean isValid(final MarketDo market) {

		if (market == null) {
			return false;
		}

		if (market.instrument().isNull()) {
			return false;
		}
		
		return true;

	}

	protected boolean isValid(final Instrument instrument) {

		if (instrument == null) {
			log.error("instrument == null");
			return false;
		}
		
		if (instrument.isNull()) {
			log.error("instrument.isNull()");
			return false;
		}
		
		@SuppressWarnings("deprecation")
		final Fraction fraction = instrument.displayFraction();

		if(fraction == null || fraction.isNull()) {
			log.error("Fraction is null");
			return false;
		}

		return true;

	}

	/* ***** ***** Unsupported ***** ***** */

	@Override
	public void startup() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutdown() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void bindConnectionStateListener(final Monitor listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void bindTimestampListener(final TimestampListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void appendMarketProvider(final MarketFactory marketFactory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized final void copyTo(
			final MarketMakerProvider<Message> maker,
			final MarketField<?>... fields) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public <V extends Value<V>> boolean register(final MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends Value<V>> boolean unregister(final MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends Value<V>> boolean update(final MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRegistered(final MarketTaker<?> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(final MarketRegListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(final MarketRegListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void notifyRegListeners() {
		throw new UnsupportedOperationException();
	}

}
