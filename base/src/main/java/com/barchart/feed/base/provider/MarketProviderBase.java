package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection.Monitor;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.participant.FrameworkAgent;
import com.barchart.feed.base.provider.MarketDataGetters.MDGetter;
import com.barchart.feed.base.sub.Subscription;
import com.barchart.feed.base.sub.SubscriptionHandler;
import com.barchart.feed.base.sub.SubscriptionType;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueConst;

public abstract class MarketProviderBase<Message extends MarketMessage> 
		implements MarketService, MarketMakerProvider<Message> {
	
	protected static final Logger log = LoggerFactory.getLogger(
			MarketProviderBase.class);

	protected final MarketFactory factory;
	protected final SubscriptionHandler subHandler;
	
	protected final ConcurrentMap<InstrumentID, MarketDo> marketMap = 
			new ConcurrentHashMap<InstrumentID, MarketDo>();
	
	protected final ConcurrentMap<String, InstrumentID> symbolMap = 
			new ConcurrentHashMap<String, InstrumentID>();

	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agents = 
			new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();
	
	protected MarketProviderBase(final MarketFactory factory,
			final SubscriptionHandler handler) {
		this.factory = factory;
		subHandler = handler;
	}
	
	/* ***** ***** Consumer Agent ***** ***** */
	
	@Override
	public <V extends MarketData<V>> ConsumerAgent register(
			MarketObserver<V> callback, Class<V> clazz) {
		
		final MDGetter<V> getter = MarketDataGetters.get(clazz);

		if (getter == null) {
			throw new IllegalArgumentException("Illegal class type "
					+ clazz.getName());
		}
		
		return null;
	}
	
	/* ***** ***** Subscription Aggregation Methods ***** ***** */
	
	private final Map<String, Set<Set<SubscriptionType>>> subs = 
			new HashMap<String, Set<Set<SubscriptionType>>>();

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

	private Subscription subscribe(final FrameworkAgent<?> agent,
			final String interest) {

		if (!agentMap.containsKey(agent)) {
			agentMap.put(agent, SubscriptionType.mapMarketEvent(agent.type()));
		}

		final Set<SubscriptionType> newSubs = agentMap.get(agent);

		if (!subs.containsKey(interest) && !newSubs.isEmpty()) {
			subs.put(interest, new HashSet<Set<SubscriptionType>>());
		}

		final Set<SubscriptionType> stuffToAdd = EnumSet.copyOf(newSubs);
		stuffToAdd.removeAll(aggregate(interest));

		if (!stuffToAdd.isEmpty()) {
			return new SubscriptionBase(interest, Subscription.Type.INSTRUMENT, stuffToAdd);
		} else {
			return Subscription.NULL;
		}

	}

	private Set<Subscription> subscribe(final FrameworkAgent<?> agent,
			final Set<String> interests) {

		final Set<Subscription> newSubs = new HashSet<Subscription>();

		for (final String interest : interests) {
			final Subscription sub = subscribe(agent, interest);
			if (!sub.isNull()) {
				newSubs.add(sub);
			}
		}

		return newSubs;

	}

	private Subscription unsubscribe(final FrameworkAgent<?> agent,
			final String interest) {

		if (!agentMap.containsKey(agent)) {
			return Subscription.NULL;
		}

		final Set<SubscriptionType> oldSubs = agentMap.remove(agent);

		subs.get(interest).remove(oldSubs);

		if (subs.get(interest).isEmpty()) {
			subs.remove(interest);
		}

		final Set<SubscriptionType> stuffToRemove = EnumSet.copyOf(oldSubs);
		stuffToRemove.removeAll(aggregate(interest));

		if (!stuffToRemove.isEmpty()) {
			return new SubscriptionBase(interest, Subscription.Type.INSTRUMENT, stuffToRemove);
		} else {
			return Subscription.NULL;
		}

	}

	private Set<Subscription> unsubscribe(final FrameworkAgent<?> agent,
			final Set<String> interests) {

		final Set<Subscription> newSubs = new HashSet<Subscription>();

		for (final String interest : interests) {
			final Subscription sub = unsubscribe(agent, interest);
			if (!sub.isNull()) {
				newSubs.add(sub);
			}
		}

		return newSubs;

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
	
	/* ***** ***** Agent Lifecycle Methods ***** ***** */
	
	private synchronized void attachAgent(final FrameworkAgent<?> agent) {

		if (agents.containsKey(agent)) {

			updateAgent(agent);

		} else {

			agents.put(agent, new Boolean(false));

			for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
				e.getValue().attachAgent(agent);
			}

		}
	}
	
	public synchronized void updateAgent(final FrameworkAgent<?> agent) {

		if (!agents.containsKey(agent)) {

			attachAgent(agent);

		} else {

			for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
				e.getValue().updateAgent(agent);
			}

		}

	}
	
	public synchronized void detachAgent(final FrameworkAgent<?> agent) {

		if (!agents.containsKey(agent)) {
			return;
		}

		agents.remove(agent);

		for (final Entry<InstrumentID, MarketDo> e : marketMap.entrySet()) {
			e.getValue().detachAgent(agent);
		}

	}
	
	// ######################## // ########################
	
	@Override
	public Observable<Market> snapshot(InstrumentID instrument) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// ######################## // ########################


	@Override
	public Observable<Result<Instrument>> instrument(String... symbols) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<Result<Instrument>> instrument(SearchContext ctx,
			String... symbols) {
		// TODO Auto-generated method stub
		return null;
	}

	// ######################## // ########################
	
	@Override
	public boolean register(Instrument instrument) {
		
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
	public boolean unregister(Instrument instrument) {
		
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
	public boolean isRegistered(Instrument instrument) {
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

		final MarketDo market = marketMap.get(instrument);

		if (!isValid(market)) {
			return;
		}

		market.runSafe(safeMake, message);

	}

	protected MarketSafeRunner<Void, Message> safeMake = new MarketSafeRunner<Void, Message>() {
		@Override
		public Void runSafe(final MarketDo market, final Message message) {
			make(message, market);
			market.fireEvents();
			return null;
		}
	};
	
	protected abstract void make(Message message, MarketDo market);
	
	// ######################## Take ########################
	
	@SuppressWarnings("unchecked")
	@Override
	public <S extends Instrument, V extends Value<V>> V take(S instrument,
			MarketField<V> field) {
		
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
			log.debug("market == null");
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

		final Price priceStep = instrument.tickSize();

		if (priceStep.isZero()) {
			log.error("priceStep.isZero()");
			return false;
		}

		final Fraction fraction = instrument.displayFraction();
		
		if(fraction == null || fraction.isNull()) {
			log.error("fraction.isNull()");
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
	public void bindConnectionStateListener(Monitor listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void bindTimestampListener(TimestampListener listener) {
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
	public <V extends Value<V>> boolean register(MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends Value<V>> boolean unregister(MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V extends Value<V>> boolean update(MarketTaker<V> taker) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRegistered(MarketTaker<?> taker) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(MarketRegListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(MarketRegListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void notifyRegListeners() {
		throw new UnsupportedOperationException();
	}
	
}
