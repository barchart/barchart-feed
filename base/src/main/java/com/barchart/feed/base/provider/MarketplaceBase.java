package com.barchart.feed.base.provider;

import java.util.ArrayList;
import java.util.Collections;
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

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.AgentFactory;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.SnapshotService;
import com.barchart.feed.api.consumer.ConsumerAgent;
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
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.MetadataID;
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
import com.barchart.feed.base.sub.Sub;
import com.barchart.feed.base.sub.SubscriptionHandler;
import com.barchart.feed.base.sub.SubscriptionType;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.inst.InstrumentService;
import com.barchart.util.value.api.Fraction;

public abstract class MarketplaceBase<Message extends MarketMessage> implements
		MarketMakerProvider<Message>, FrameworkAgentLifecycleHandler,
		SnapshotService, AgentFactory {

	protected static final Logger log = LoggerFactory
			.getLogger(MarketplaceBase.class);

	protected final MarketFactory factory;
	protected final InstrumentService<String> instLookup;
	protected final SubscriptionHandler subHandler;

	protected final ConcurrentMap<InstrumentID, MarketDo> marketMap = 
			new ConcurrentHashMap<InstrumentID, MarketDo>();
	
	protected final ConcurrentMap<String, InstrumentID> symbolMap = 
			new ConcurrentHashMap<String, InstrumentID>();

	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agents = 
			new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();

	protected MarketplaceBase(final MarketFactory factory,
			final InstrumentService<String> instLookup,
			final SubscriptionHandler handler) {

		this.factory = factory;
		this.instLookup = instLookup;
		this.subHandler = handler;
		
	}

	// #########################

	@Override
	public <V extends MarketData<V>> Agent newAgent(final Class<V> clazz,
			final MarketObserver<V> callback) {

		final MDGetter<V> getter = MarketDataGetters.get(clazz);

		if (getter == null) {
			throw new IllegalArgumentException("Illegal class type "
					+ clazz.getName());
		}

		final FrameworkAgent<V> agent = new BaseAgent<V>(this, clazz, getter, callback);

		attachAgent(agent);

		return agent.userAgent();
	}

	private class BaseAgent<V extends MarketData<V>> implements
			FrameworkAgent<V>, Agent {

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
		
		BaseAgent(final FrameworkAgentLifecycleHandler agentHandler,
				final Class<V> clazz, final MDGetter<V> getter,
				final MarketObserver<V> callback) {

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
		public Agent userAgent() {
			return this;
		}
		
		@Override 
		public ConsumerAgent consumerAgent() {
			throw new UnsupportedOperationException();
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
				interests.add(i.symbol());
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

			@Override
			public boolean hasMatch(Instrument instrument) {
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
					// TODO FIXME
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
			state = State.TERMINATED;
			agentHandler.detachAgent(this);
		}

		@Override
		public synchronized void include(final String... symbols) {

			final Set<String> symbSet = new HashSet<String>();
			Collections.addAll(symbSet, symbols);

			final Map<String, Instrument> instMap = instLookup
					.lookup(symbSet);
			final Set<String> newInterests = new HashSet<String>();

			for (final Entry<String, Instrument> e : instMap
					.entrySet()) {

				final Instrument i = e.getValue();

				if (!i.isNull()) {

					exInsts.remove(i);
					incInsts.add(i);
					
					newInterests.add(formatForJERQ(i.symbol()));
					
				} else {
					/*
					 * For all failed lookups, store symbol and attempt to match 
					 * in the hasMatch method.
					 */
					incUnknown.add(e.getKey().toString());
					exUnknown.remove(e.getKey().toString());
				}
				
			}

			agentHandler.updateAgent(this);

			final Set<Sub> newSubs = subscribe(this, newInterests);
			if (!newSubs.isEmpty()) {
				log.debug("Sending new subs to sub handler");
				subHandler.subscribe(newSubs);
			}

		}
		
		@Override
		public synchronized void exclude(final String... symbols) {

			final Set<String> symbSet = new HashSet<String>();
			Collections.addAll(symbSet, symbols);

			final Map<String, Instrument> instMap = instLookup
					.lookup(symbSet);

			final Set<String> oldInterests = new HashSet<String>();

			for (final Entry<String, Instrument> e : instMap
					.entrySet()) {

				final Instrument i = e.getValue();

				if (!i.isNull()) {

					incInsts.remove(i);
					exInsts.add(i);

					oldInterests.add(i.symbol());
					
				} else {
					/*
					 * For all failed lookups, store symbol and attempt to match 
					 * in the hasMatch method.
					 */
					incUnknown.remove(e.getKey().toString());
					exUnknown.add(e.getKey().toString());
				}

			}

			agentHandler.updateAgent(this);

			final Set<Sub> oldSubs = unsubscribe(this, oldInterests);
			if (!oldSubs.isEmpty()) {
				log.debug("Sending new unsubs to sub handler");
				subHandler.unsubscribe(oldSubs);
			}

		}
		
		/* ***** ***** Filter Updatable ***** ***** */
		
		@Override
		public void filter(Filter filter) {
			this.filter = filter;
		}
		
		@Override
		public Filter filter() {
			return filter;
		}

		@Override
		@Deprecated
		public synchronized void include(final Metadata... meta) {

			final Set<String> newInterests = new HashSet<String>();

			for(Metadata m : meta) {
				
				if(m.isNull()) {
					continue;
				}
				
				switch(m.type()) {
				
				default:
					// Ignore 
					continue;
				case INSTRUMENT:
					incInsts.add((Instrument)m);
					exInsts.remove(m);
					newInterests.add(formatForJERQ(((Instrument)m).symbol()));
					continue;
				case EXCHANGE:
					incExchanges.add((Exchange)m);
					exExchanges.remove(m);
					newInterests.add(((Exchange)m).id().toString());
				}
			
			}

			agentHandler.updateAgent(this);

			final Set<Sub> newSubs = subscribe(this, newInterests);
			if (!newSubs.isEmpty()) {
				log.debug("Sending new subs to sub handler");
				subHandler.subscribe(newSubs);
			}

		}
		
		@Override
		public void include(final MetadataID<?>... metaID) {
			
			final List<InstrumentID> ids = new ArrayList<InstrumentID>();
			
			for(final MetadataID<?> m : metaID) {
				
				if(m.metaType() == MetaType.INSTRUMENT) {
					ids.add((InstrumentID) m);
				}
				
			}
			
			final List<Instrument> insts = new ArrayList<Instrument>();
			
			for(final InstrumentID id : ids) {
				final Instrument i = instLookup.lookup(id);
				if(!i.isNull()) {
					insts.add(i);
				}
			}
			
			include(insts.toArray(new Instrument[0]));
			
		}

		@Override
		@Deprecated
		public synchronized void exclude(final Metadata... meta) {

			final Set<String> oldInterests = new HashSet<String>();

			for(Metadata m : meta) {
				
				if(m.isNull()) {
					continue;
				}
				
				switch(m.type()) {
				
				default:
					// Ignore 
					continue;
				case INSTRUMENT:
					exInsts.add((Instrument)m);
					incInsts.remove(m);
					oldInterests.add(formatForJERQ(((Instrument)m).symbol()));
					continue;
				case EXCHANGE:
					exExchanges.add((Exchange)m);
					incExchanges.remove(m);
					oldInterests.add(((Exchange)m).id().toString());
				}
				
			}

			agentHandler.updateAgent(this);

			final Set<Sub> oldSubs = unsubscribe(this, oldInterests);
			if (!oldSubs.isEmpty()) {
				log.debug("Sending new unsubs to sub handler");
				subHandler.unsubscribe(oldSubs);
			}

		}
		
		@Override
		public void exclude(final MetadataID<?>... metaID) {
			
			final List<InstrumentID> ids = new ArrayList<InstrumentID>();
			
			for(final MetadataID<?> m : metaID) {
				
				if(m.metaType() == MetaType.INSTRUMENT) {
					ids.add((InstrumentID) m);
				}
				
			}
			
			final List<Instrument> insts = new ArrayList<Instrument>();
			
			for(final InstrumentID id : ids) {
				final Instrument i = instLookup.lookup(id);
				if(!i.isNull()) {
					insts.add(i);
				}
			}
			
			exclude(insts.toArray(new Instrument[0]));
			
		}

		@Override
		public synchronized void clear() {

			incInsts.clear();
			exInsts.clear();
			incExchanges.clear();
			exExchanges.clear();

			agentHandler.updateAgent(this);

		}

	}

	/* ***** ***** Subscription Aggregation Methods ***** ***** */

	private final Map<String, List<Set<SubscriptionType>>> subs = 
			new HashMap<String, List<Set<SubscriptionType>>>();

	private final Map<FrameworkAgent<?>, Set<SubscriptionType>> agentMap = 
			new HashMap<FrameworkAgent<?>, Set<SubscriptionType>>();

	private Set<SubscriptionType> aggregate(final String interest) {

		final Set<SubscriptionType> agg = EnumSet
				.noneOf(SubscriptionType.class);

		if (!subs.containsKey(interest)) {
			return agg;
		}

		for (final Set<SubscriptionType> set : subs.get(interest)) {
			agg.addAll(set);
		}

		return agg;
	}

	protected Sub subscribe(final FrameworkAgent<?> agent,
			final String interest) {

		if (!agentMap.containsKey(agent)) {
			agentMap.put(agent, SubscriptionType.mapMarketEvent(agent.type()));
		}

		final Set<SubscriptionType> newSubs = agentMap.get(agent);

		if (!subs.containsKey(interest) && !newSubs.isEmpty()) {
			subs.put(interest, new RefEqualsList<Set<SubscriptionType>>());
		}
		
		final Set<SubscriptionType> stuffToAdd = EnumSet.copyOf(newSubs);
		stuffToAdd.removeAll(aggregate(interest));
		
		subs.get(interest).add(newSubs);

		if (!stuffToAdd.isEmpty()) {
			return new SubBase(interest, Sub.Type.INSTRUMENT, stuffToAdd);
		} else {
			return Sub.NULL;
		}

	}

	protected Set<Sub> subscribe(final FrameworkAgent<?> agent,
			final Set<String> interests) {

		final Set<Sub> newSubs = new HashSet<Sub>();

		for (final String interest : interests) {
			final Sub sub = subscribe(agent, interest);
			if (!sub.isNull()) {
				newSubs.add(sub);
			}
		}

		return newSubs;

	}

	protected Sub unsubscribe(final FrameworkAgent<?> agent,
			final String interest) {

		if (!agentMap.containsKey(agent)) {
			return Sub.NULL;
		}

		final Set<SubscriptionType> oldSubs = agentMap.get(agent);

		subs.get(interest).remove(oldSubs);

//		if (subs.get(interest).isEmpty()) {
//			subs.remove(interest);
//		}

		final Set<SubscriptionType> stuffToRemove = EnumSet.copyOf(oldSubs);
		stuffToRemove.removeAll(aggregate(interest));

		if (!stuffToRemove.isEmpty()) {
			return new SubBase(interest, Sub.Type.INSTRUMENT, stuffToRemove);
		} else {
			return Sub.NULL;
		}

	}

	protected Set<Sub> unsubscribe(final FrameworkAgent<?> agent,
			final Set<String> interests) {

		final Set<Sub> newSubs = new HashSet<Sub>();

		for (final String interest : interests) {
			final Sub sub = unsubscribe(agent, interest);
			if (!sub.isNull()) {
				newSubs.add(sub);
			}
		}

		return newSubs;

	}
	
	class RefEqualsList<T> extends ArrayList<T> {
		
		private static final long serialVersionUID = -7398964176381704808L;

		@Override
		public boolean equals(Object o) {
			if(this == o) {
				return true;
			} else {
				return false;
			}
		}
		
	}

	private static String formatForJERQ(final String symbol) {

		log.debug("Formatting {} for JERQ", symbol);

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
	
	/* ***** ***** SnapshotProvider ***** ***** */
	
	@Override
	public Market snapshot(final Instrument instrument) {
		
		if(marketMap.containsKey(instrument.id())) {
			return marketMap.get(instrument.id()).freeze();
		}
		
		return Market.NULL;
	}
	
	@Override
	public Market snapshot(final InstrumentID instID) {
		
		if(marketMap.containsKey(instID)) {
			return marketMap.get(instID).freeze();
		}
		
		return Market.NULL;
		
	}
	
	@Override
	public Market snapshot(final String symbol) {
		
		if(symbolMap.containsKey(Symbology.formatSymbol(symbol))) {
			return marketMap.get(
					symbolMap.get(Symbology.formatSymbol(symbol))).freeze();
		}
		
		return Market.NULL;
	}

	/* ***** ***** Agent Lifecycle Methods ***** ***** */

	/*
	 * TODO These only should be called from inside framework agents, so some
	 * re-structuring may be needed
	 */
	@Override
	public synchronized void attachAgent(final FrameworkAgent<?> agent) {

		if (agents.containsKey(agent)) {

			updateAgent(agent);

		} else {

			agents.put(agent, new Boolean(false));

			for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
				e.getValue().attachAgent(agent);
			}

		}
	}

	/*
	 * TODO These only should be called from inside framework agents, so some
	 * re-structuring may be needed
	 */
	@Override
	public synchronized void updateAgent(final FrameworkAgent<?> agent) {

		if (!agents.containsKey(agent)) {

			attachAgent(agent);

		} else {

			for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
				e.getValue().updateAgent(agent);
			}

		}

	}

	/*
	 * TODO These only should be called from inside framework agents, so some
	 * re-structuring may be needed
	 */
	@Override
	public synchronized void detachAgent(final FrameworkAgent<?> agent) {

		if (!agents.containsKey(agent)) {
			return;
		}

		agents.remove(agent);

		for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
			e.getValue().detachAgent(agent);
		}

	}

	// ########################

	@Override
	public int marketCount() {
		return marketMap.size();
	}

	@Override
	public boolean isRegistered(final Instrument instrument) {
		return marketMap.containsKey(instrument.id());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Instrument, V extends Value<V>> V take(
			final S instrument, final MarketField<V> field) {

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

	// ########################

	@Override
	public synchronized void clearAll() {
		marketMap.clear();
		symbolMap.clear();
	}

	// ########################

	@Override
	public void make(final Message message) {

		final Instrument instrument = message.getInstrument();

		if (!isValid(instrument)) {
			return;
		}

		MarketDo market = marketMap.get(instrument.id());

		if (!isValid(market)) {
			register(instrument);
			market = marketMap.get(instrument.id());
		}

		market.runSafe(safeMake, message);

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

	// ########################

	@Override
	public synchronized final void copyTo(
			final MarketMakerProvider<Message> maker,
			final MarketField<?>... fields) {
		throw new UnsupportedOperationException("TODO");
	}

	// ########################

	@Override
	public void appendMarketProvider(final MarketFactory marketFactory) {
		throw new UnsupportedOperationException("TODO");
	}

	// ########################

	@Override
	public final synchronized boolean register(final Instrument instrument) {

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
			
		} else {
			log.warn("already registered : {}", instrument.id());
		}

		return wasAdded;
	}

	@Override
	public final synchronized boolean unregister(final Instrument instrument) {

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

	/* ***** ***** Validation ***** ***** */

	protected boolean isValid(final MarketDo market) {

		if (market == null) {
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
			return false;
		}

		final Fraction fraction = instrument.displayFraction();

		if (fraction == null || fraction.isNull()) {
			log.error("fraction.isNull()");
			return false;
		}

		return true;

	}

	/* ***** ***** Unsupported ***** ***** */

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

	@Override
	public boolean isRegistered(final MarketTaker<?> taker) {
		throw new UnsupportedOperationException();
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

}
