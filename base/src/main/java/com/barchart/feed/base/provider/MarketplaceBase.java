package com.barchart.feed.base.provider;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.AgentBuilder;
import com.barchart.feed.api.AgentLifecycleHandler;
import com.barchart.feed.api.FrameworkAgent;
import com.barchart.feed.api.MarketCallback;
import com.barchart.feed.api.SubscriptionManager;
import com.barchart.feed.api.data.Exchange;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;
import com.barchart.feed.api.inst.InstrumentService;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.value.api.Price;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.Value;

public abstract class MarketplaceBase<Message extends MarketMessage> implements
		MarketMakerProvider<Message>, AgentBuilder, AgentLifecycleHandler {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected final MarketFactory factory;
	protected final InstrumentService<CharSequence> instLookup;
	protected final SubscriptionManager subManager = new SubscriptionManager();
	
	// TODO Review concurrency, only reason to use ConcurrentMap is for 
	// putIfAbscent()
	private final ConcurrentMap<Instrument, MarketDo> marketMap = 
		new ConcurrentHashMap<Instrument, MarketDo>();
	
	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agents = 
		new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();
	
	protected MarketplaceBase(final MarketFactory factory, 
			final InstrumentService<CharSequence> instLookup) {
		
		this.factory = factory;
		this.instLookup = instLookup;
		
	}
	
	// ########################
	
	@Override
	public <V extends MarketData> Agent newAgent(final Class<V> clazz, 
			final MarketCallback<V> callback, final MarketEventType... types) {
		
		final FrameworkAgent<V> agent = new BaseAgent<V>(null, clazz, callback, types);
		
		attachAgent(agent);
		
		return agent;
	}
		
	private class BaseAgent<V extends MarketData> implements FrameworkAgent<V> {

		private final AtomicBoolean isActive = new AtomicBoolean(true);
		
		private final AgentLifecycleHandler agentHandler;
		private final MarketCallback<V> callback;
		private final MarketEventType[] types;
		
		// Review concurrency
		private final AtomicBoolean allInstruments = new AtomicBoolean(false);
		
		private final Set<Exchange> incExchanges = new HashSet<Exchange>();
		private final Set<Exchange> exExchanges = new HashSet<Exchange>();
		
		private final Set<Instrument> incInsts = new HashSet<Instrument>();
		private final Set<Instrument> exInsts = new HashSet<Instrument>();
		
		BaseAgent(final AgentLifecycleHandler agentHandler, final Class<V> clazz, 
				final MarketCallback<V> callback, final MarketEventType... types) {
			
			this.agentHandler = agentHandler;
			this.callback = callback;
			this.types = types;
			
		}
		
		/* ***** ***** Framework Methods ***** ***** */
		
		@Override
		public MarketEventType[] eventTypes() {
			return types;
		}

		@Override
		public MarketCallback<V> callback() {
			return callback;
		}

		@Override
		public V data(final Market market) {
			// Methods? Hell yea!
			// Waiting on Andrei
			return null;
		}
		
		/* ***** ***** Filter Methods ***** ***** */
		
		@Override
		public boolean accept(final Instrument instrument) {
			
			/* Work bottom up on the hierarchy */
			
			// TODO Custom filters
			
			if(incInsts.contains(instrument)) {
				return true;
			}
			
			if(exInsts.contains(instrument)) {
				return false;
			}
			
			if(incExchanges.contains(instrument.exchange())) {
				return true;
			}
			
			if(exExchanges.contains(instrument.exchange())) {
				return false;
			}
			
			if(allInstruments.get()) {
				return true;
			}
			
			return false;
		}
		
		@Override
		public Instrument[] instruments() {
			return incInsts.toArray(new Instrument[0]);
		}
		
		@Override
		public Exchange[] exchanges() {
			return incExchanges.toArray(new Exchange[0]);
		}
		
		@Override
		public boolean all() {
			return allInstruments.get();
		}

		/* ***** ***** Consumer Methods ***** ***** */
		
		@Override
		public boolean isActive() {
			return isActive.get();
		}
		
		@Override
		public void activate() {
			isActive.set(true);
		}

		@Override
		public void deactivate() {
			isActive.set(false);
		}

		@Override
		public synchronized void dismiss() {
			agentHandler.detachAgent(this);
		}

		@Override
		public synchronized void includeAll() {
			allInstruments.set(true);
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void include(final CharSequence... symbols) {
			final Set<CharSequence> symbSet = new HashSet<CharSequence>();
			Collections.addAll(symbSet, symbols);
			
			final Map<CharSequence, Instrument> instMap = instLookup.lookup(symbSet);
			
			for(final Entry<CharSequence, Instrument> e : instMap.entrySet()) {
				final Instrument i = e.getValue();
				exInsts.remove(i);
				incInsts.add(i);
			}
			
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void include(final Instrument... instruments) {
			
			for(final Instrument i : instruments) {
				exInsts.remove(i);
				incInsts.add(i);
			}
			
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void include(final Exchange... exchanges) {
			
			for(final Exchange e : exchanges) {
				exExchanges.remove(e);
				incExchanges.add(e);
			}
			
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void exclude(final CharSequence... symbols) {
			
			final Set<CharSequence> symbSet = new HashSet<CharSequence>();
			Collections.addAll(symbSet, symbols);
			
			final Map<CharSequence, Instrument> instMap = instLookup.lookup(symbSet);
			
			for(final Entry<CharSequence, Instrument> e : instMap.entrySet()) {
				final Instrument i = e.getValue();
				incInsts.remove(i);
				exInsts.add(i);
			}
			
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void exclude(final Instrument... instruments) {
			
			for(final Instrument i : instruments) {
				incInsts.remove(i);
				exInsts.add(i);
			}
			
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void exclude(final Exchange... exchanges) {
			
			for(final Exchange e : exchanges) {
				incExchanges.remove(e);
				exExchanges.add(e);
			}
			
			agentHandler.updateAgent(this);
		}

		@Override
		public synchronized void clear() {
			
			allInstruments.set(false);
			incInsts.clear();
			exInsts.clear();
			incExchanges.clear();
			exExchanges.clear();
			
			agentHandler.updateAgent(this);
		}

		
	}

	@Override
	public synchronized void attachAgent(final FrameworkAgent<?> agent) {
		
		if(agents.containsKey(agent)) {
			
			updateAgent(agent);
			
		} else {
		
			agents.put(agent, new Boolean(false));
			subManager.attachAgent(agent);
			
			for(final Entry<Instrument, MarketDo> e : marketMap.entrySet()) {
				e.getValue().attachAgent(agent);
			}
			
		}
	}
	
	@Override
	public synchronized void updateAgent(final FrameworkAgent<?> agent) {
		
		if(!agents.containsKey(agent)) {
		
			attachAgent(agent);
			
		} else {
			
			subManager.updateAgent(agent);
			
			for(final Entry<Instrument, MarketDo> e : marketMap.entrySet()) {
				e.getValue().updateAgent(agent);
			}
		
		}
		
	}
	
	@Override
	public synchronized void detachAgent(final FrameworkAgent<?> agent) {
		
		if(!agents.containsKey(agent)) {
			return;
		}
		
		agents.remove(agent);
		subManager.detachAgent(agent);
		
		for(final Entry<Instrument, MarketDo> e : marketMap.entrySet()) {
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
		return marketMap.containsKey(instrument);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Instrument, V extends Value<V>> V take(final S instrument,
			final MarketField<V> field) {
		
		final MarketDo market = marketMap.get(instrument);

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
	}

	// ########################
	
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
	
	protected MarketSafeRunner<Void, Message> safeMake = 
		new MarketSafeRunner<Void, Message>() {
			@Override
			public Void runSafe(final MarketDo market, final Message message) {
				make(message, market);
				market.fireEvents();
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

		MarketDo market = marketMap.get(instrument);

		final boolean wasAdded = (market == null);

		while (market == null) {
			market = factory.newMarket();
			market.setInstrument(instrument);
			marketMap.putIfAbsent(instrument, market);
			market = marketMap.get(instrument);
		}

		if (wasAdded) {
			
			for(final FrameworkAgent<?> agent : agents.keySet()) {
				marketMap.get(instrument).attachAgent(agent);
			}
			
		} else {
			log.warn("already registered : {}", instrument);
		}

		
		
		return wasAdded;
	}

	@Override
	public final synchronized boolean unregister(final Instrument instrument) {
		
		if (!isValid(instrument)) {
			return false;
		}

		final MarketDo market = marketMap.remove(instrument);

		final boolean wasRemoved = (market != null);

		if (wasRemoved) {
			
			for(final FrameworkAgent<?> agent : agents.keySet()) {
				marketMap.get(instrument).detachAgent(agent);
			}
			
		} else {
			log.warn("was not registered : {}", instrument);
		}

		return wasRemoved;

	}
	
	/* ***** ***** Validation ***** ***** */
	
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
	
	@Override
	public boolean isRegistered(MarketTaker<?> taker) {
		throw new UnsupportedOperationException();
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

}
