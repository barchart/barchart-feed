package com.barchart.feed.base.provider;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.fields.InstrumentField;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.NEWMarketAgent;
import com.barchart.feed.base.market.api.NEWMarketMaker;
import com.barchart.feed.base.market.api.NEW_MarketDo;
import com.barchart.feed.base.market.api.NEW_MarketFactory;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.Value;

public abstract class NEW_MakerBase<Message extends MarketMessage> 
		implements NEWMarketMaker<Message> {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected final NEW_MarketFactory factory;
	
	final ConcurrentMap<Instrument, NEW_MarketDo> marketMap = 
			new ConcurrentHashMap<Instrument, NEW_MarketDo>();
	
	final ConcurrentMap<NEWMarketAgent<Message, ?>, Void> agentMap =
			new ConcurrentHashMap<NEWMarketAgent<Message, ?>, Void>();
	
	private final CopyOnWriteArrayList<MarketRegListener> listenerList = 
			new CopyOnWriteArrayList<MarketRegListener>();
	
	protected NEW_MakerBase(final NEW_MarketFactory factory) {
		this.factory = factory;
	}
	
	/* ***** ***** ***** ***** ***** ***** */
	
	@Override
	public int marketCount() {
		return marketMap.size();
	}
	
	@Override
	public boolean isRegistered(final Instrument instrument) {
		return marketMap.containsKey(instrument);
	}
	
	@Override
	public boolean isRegistered(final NEWMarketAgent<Message, ?> agent) {
		return agentMap.containsKey(agent);
	}
	
	/* ***** ***** ***** ***** ***** ***** */

	@Override
	public synchronized final <V extends Value<V>> boolean register(
			final NEWMarketAgent<Message, V> agent) {
		
		//Validate agent
		
		/* Add agent to map */
		agentMap.putIfAbsent(agent, null);
		
		/* Attach agent to all registered instruments */
		for(final Entry<Instrument, NEW_MarketDo> e : marketMap.entrySet()) {
			
			final Instrument inst = e.getKey();
			final NEW_MarketDo market = e.getValue();
			
			if (!isValid(inst)) {
				continue;
			}
			
			if(!isRegistered(inst)) {
				register(inst);
			}
			
			market.addAgent(agent);
			
			notifyRegListeners(market);
			
		}
		
		//TODO Re-examine this
		return true;
	}

	@Override
	public synchronized <V extends Value<V>> boolean unregister(
			final NEWMarketAgent<Message, V> agent) {
		
		if(agent == null) {
			return false;
		}
		
		agentMap.remove(agent);
		agent.dismiss();
		
		//TODO Re-examine this
		return true;
	}
	
	/* ***** ***** ***** ***** ***** ***** */

	@Override
	public void make(final Message message) {
		
		final Instrument instrument = message.getInstrument();

		if (!isValid(instrument)) {
			return;
		}

		final NEW_MarketDo market = marketMap.get(instrument);

		if(!isValid(market)) {
			return;
		}
		
		// Update Market State
		make(message, market);
		
		// Signal Agents to fire callbacks
		market.fireEvents();
		
	}
	
	protected abstract void make(Message message, NEW_MarketDo market);

	/* ***** ***** ***** ***** ***** ***** */
	
	@Override
	public synchronized final void clearAll() {
		marketMap.clear();
		
		// Shouldn't need to dismiss agents
		agentMap.clear();
	}

	/* ***** ***** ***** ***** ***** ***** */
	
	@Override
	public final synchronized void register(final Instrument instrument) {
		
		if (!isValid(instrument)) {
			return;
		}
		
		if(marketMap.containsKey(instrument)) {
			return;
		}
		
		final NEW_MarketDo market = factory.newMarket(instrument);
		marketMap.put(instrument, market);
		
		for(final NEWMarketAgent<?,?> agent : agentMap.keySet()) {
			market.addAgent(agent);
		}
		
	}
	
	@Override
	public final synchronized void unregister(final Instrument instrument) {
		
		if (instrument != null) {
			marketMap.remove(instrument);
		}
		
	}
	
	/* ***** ***** ***** ***** ***** ***** */

	@Override
	public void add(final MarketRegListener listener) {
		listenerList.addIfAbsent(listener);
	}

	@Override
	public void remove(final MarketRegListener listener) {
		listenerList.remove(listener);
	}

	@Override
	public void notifyRegListeners() {
		for(final NEW_MarketDo market: marketMap.values()) {
			notifyRegListeners(market);
		}
	}

	/* ***** ***** ***** Non-public methods ***** ***** ***** */
	
	protected final void notifyRegListeners(final NEW_MarketDo market) {

		final Instrument inst = market.get(MarketField.INSTRUMENT);

		final Set<MarketEvent> events = market.events();

		for (final MarketRegListener listener : listenerList) {
			try {
				listener.onRegistrationChange(inst, events);
			} catch (final Exception e) {
				log.error("", e);
			}
		}

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

		final PriceValue priceStep = instrument.get(InstrumentField.PRICE_STEP);

		if (priceStep.isZero()) {
			log.error("priceStep.isZero()");
			return false;
		}

		final Fraction fraction = instrument.get(InstrumentField.DISPLAY_FRACTION);
		
		if(fraction == null || fraction.isNull()) {
			log.error("fraction.isNull()");
			return false;
		}

		return true;

	}
	
	protected boolean isValid(final NEW_MarketDo market) {

		if (market == null) {
			log.debug("market == null");
			return false;
		}

		return true;

	}
	
}
