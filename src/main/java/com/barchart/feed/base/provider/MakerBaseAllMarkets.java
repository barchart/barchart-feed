package com.barchart.feed.base.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketRegListener;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.Value;

public abstract class MakerBaseAllMarkets<Message extends MarketMessage> 
	implements MarketMakerProvider<Message> {


	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected final MarketFactory factory;
	
	private final ConcurrentMap<MarketInstrument, VarMarket> marketMap = //
			new ConcurrentHashMap<MarketInstrument, VarMarket>();
	
	// Don't need RegTaker
	private final ConcurrentMap<MarketTaker<?>, RegTaker<?>> takerMap = //
			new ConcurrentHashMap<MarketTaker<?>, RegTaker<?>>();
	
	private final EventMap<RegTakerList> eventTakerMap = new EventMap<RegTakerList>();
	
	// ########################
	
	
	protected MakerBaseAllMarkets(final MarketFactory factory) {
		this.factory = factory;
		
		for(final MarketEvent event : MarketEvent.values()) {
			eventTakerMap.put(event, new RegTakerList());
		}
	}
	
	@Override
	public int marketCount() {
		return marketMap.size();
	}
	
	@Override
	public boolean isRegistered(final MarketInstrument instrument) {
		return marketMap.containsKey(instrument);
	}
	
	@Override
	public boolean isRegistered(final MarketTaker<?> taker) {
		return takerMap.containsKey(taker);
	}
	
	
	// ########################
	
	
	@Override
	public synchronized final <V extends Value<V>> boolean register(
			final MarketTaker<V> taker) {
		
		if (!RegTaker.isValid(taker)) {
			return false;
		}

		RegTaker<?> regTaker = takerMap.get(taker);

		final boolean wasAdded = (regTaker == null);

		while (regTaker == null) {
			regTaker = new RegTaker<V>(taker);
			takerMap.putIfAbsent(taker, regTaker);
			regTaker = takerMap.get(taker);
		}
	
		/* Add taker to TakerList for each MarketEvent */
		for(MarketEvent e : regTaker.getEvents()) {
			eventTakerMap.get(e).add(regTaker);
		}
		
		return wasAdded;
		
	}

	@Override
	public synchronized final <V extends Value<V>> boolean update(
			final MarketTaker<V> taker) {
		
		if (!RegTaker.isValid(taker)) {
			return false;
		}
		
		final RegTaker<?> regTaker = takerMap.get(taker);
		
		/* Purge taker from all TakerLists */
		for(MarketEvent e : MarketEvent.values()) {
			eventTakerMap.get(e).remove(regTaker);
		}
		
		/* Add taker to TakerList for each MarketEvent */
		for(MarketEvent e : regTaker.getEvents()) {
			eventTakerMap.get(e).add(regTaker);
		}
		
		return true;
	}
	
	@Override
	public synchronized final <V extends Value<V>> boolean unregister(
			final MarketTaker<V> taker) {

		if (!RegTaker.isValid(taker)) {
			return false;
		}

		final RegTaker<?> regTaker = takerMap.remove(taker);

		/* Purge taker from all TakerLists */
		for(MarketEvent e : MarketEvent.values()) {
			eventTakerMap.get(e).remove(regTaker);
		}

		return true;
		
	}
	
	// ########################
	
	
	@Override
	public final void make(final Message message) {

		final MarketInstrument instrument = message.getInstrument();
		
		if (!isValid(instrument)) {
			return;
		}
		
		VarMarket market = marketMap.get(instrument);
		
		if(!isValid(market)) {
			register(instrument);
			market = marketMap.get(instrument);
		}
		
		market.runSafe(safeMake, message);
		
	}
	
	private final MarketSafeRunner<Void, Message> safeMake = 
			new MarketSafeRunner<Void, Message>() {
				@Override
				public Void runSafe(final MarketDo market, final Message message) {
					make(message, market);
					fireEvents(market);
					return null;
				}
			};
	
	protected abstract void make(Message message, MarketDo market);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final void fireEvents(final MarketDo market) {
		
		VarMarket varMarket = (VarMarket)market;
		
		for(final MarketEvent event : varMarket.reg.eventSet) {
			
			final RegTakerList takers = eventTakerMap.get(event);
			
			for(final RegTaker taker : takers) {
				
				taker.getTaker().onMarketEvent(event, market.get(MarketField.INSTRUMENT), 
						market.get(taker.getField()));
				
			}
			
		}
		
		varMarket.reg.eventsClear();  // HACK
		
	}
	
	
	// ########################
	

	@SuppressWarnings("unchecked")
	@Override
	public final <S extends MarketInstrument, V extends Value<V>> V take(
			final S instrument, final MarketField<V> field) {

		final MarketDo market = marketMap.get(instrument);

		if (market == null) {
			return MarketConst.NULL_MARKET.get(field).freeze();
		}

		return (V) market.runSafe(safeTake, field);

	}

	private final MarketSafeRunner<Value<?>, MarketField<?>> safeTake = //
	new MarketSafeRunner<Value<?>, MarketField<?>>() {
		@Override
		public Value<?> runSafe(final MarketDo market,
				final MarketField<?> field) {
			return market.get(field).freeze();
		}
	};
	

	// ########################
	

	@Override
	public synchronized final void copyTo(
			final MarketMakerProvider<Message> maker,
			final MarketField<?>... fields) {
		throw new UnsupportedOperationException("TODO");
	}

	// ########################

	@Override
	public synchronized void clearAll() {
		marketMap.clear();
		takerMap.clear();
	}

	// ########################

	protected final boolean isValid(final MarketDo market) {

		if (market == null) {
			//log.debug("market == null");
			return false;
		}

		return true;

	}

	protected final boolean isValid(final MarketInstrument instrument) {

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

		final Fraction fraction = instrument.get(InstrumentField.FRACTION);

		if (fraction.isNull()) {
			log.error("fraction.isNull()");
			return false;
		}

		if (priceStep.exponent() != fraction.decimalExponent) {
			log.error("priceStep.exponent() != fraction.decimalExponent");
			return false;
		}

		return true;

	}


	@Override
	public final synchronized boolean register(final MarketInstrument instrument) {

		if (!isValid(instrument)) {
			return false;
		}

		VarMarket market = marketMap.get(instrument);

		final boolean wasAdded = (market == null);

		while (market == null) {
			market = (VarMarket) factory.newMarket(); // *** THIS IS A HACK ***
			market.setInstrument(instrument);
			marketMap.putIfAbsent(instrument, market);
			market = marketMap.get(instrument);
		}

		if (wasAdded) {
			//
		} else {
			log.warn("already registered : {}", instrument);
		}

		return wasAdded;

	}
	
	@Override
	public final synchronized boolean unregister(
			final MarketInstrument instrument) {
		return false;  // or true, whatever
	}
	
	// ########################
	
	
	@Override
	public void appendMarketProvider(final MarketFactory marketFactory) {
		throw new UnsupportedOperationException("TODO");
	}

	protected RegTaker<?> getRegTaker(final MarketTaker<?> taker) {
		return takerMap.get(taker);
	}

	protected MarketDo getMarket(final MarketInstrument inst) {
		return marketMap.get(inst);
	}
	
	
	// ########################
	
	@Override
	public void add(final MarketRegListener listener) {
		// No registrations
	}

	@Override
	public void remove(final MarketRegListener listener) {
		// No registrations
	}
	
	@Override
	public void notifyRegListeners() {
		// No registrations
	}
	
}
