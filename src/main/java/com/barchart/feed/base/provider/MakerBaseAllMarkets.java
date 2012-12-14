package com.barchart.feed.base.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.Value;

public abstract class MakerBaseAllMarkets<Message extends MarketMessage> 
	extends MakerBase<Message>
	implements MarketMakerProvider<Message> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final EventMap<RegTakerList> eventTakerMap = new EventMap<RegTakerList>();
	
	// ########################
	
	
	protected MakerBaseAllMarkets(final MarketFactory factory) {
		super(factory);
		
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
	
	
	public synchronized final <V extends Value<V>> boolean registerForAll(
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

	public synchronized final <V extends Value<V>> boolean updateForAll (
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
	
	public synchronized final <V extends Value<V>> boolean unregisterForAll (
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
	
	private final MarketTaker<Market> omniTaker = new MarketTaker<Market>() {

		final MarketInstrument[] blankInsts = {};
		
		@Override
		public MarketField<Market> bindField() {
			return MarketField.MARKET;
		}

		@Override
		public MarketEvent[] bindEvents() {
			return MarketEvent.values();
		}

		@Override
		public MarketInstrument[] bindInstruments() {
			return blankInsts;
		}

		@Override
		public void onMarketEvent(MarketEvent event,
				MarketInstrument instrument, Market value) {
			
			fireEvents(marketMap.get(instrument));
			
		}
		
	};
	
	private final RegTaker<Market> regOmniTaker = new RegTaker<Market>(omniTaker);
	
	@Override
	public final void make(final Message message) {

		final MarketInstrument instrument = message.getInstrument();
		
		if (!isValid(instrument)) {
			return;
		}
		
		MarketDo market = marketMap.get(instrument);
		
		if(!isValid(market)) {
			register(instrument);
			market = marketMap.get(instrument);
		}
		
		market.regAdd(regOmniTaker);
		market.runSafe(safeMake, message);
		
	}
	
	private final MarketSafeRunner<Void, Message> safeMake = 
			new MarketSafeRunner<Void, Message>() {
				@Override
				public Void runSafe(final MarketDo market, final Message message) {
					make(message, market);
					market.fireEvents();
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
	
	protected boolean isValid(final MarketDo market) {

		if (market == null) {
			return false;
		}

		return true;
	}


	protected boolean isValid(final MarketInstrument instrument) {

		if (instrument == null) {
			return false;
		}

		if (instrument.isNull()) {
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

}
