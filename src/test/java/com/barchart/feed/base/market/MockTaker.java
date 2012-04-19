/**
 * 
 */
package com.barchart.feed.base.market;

import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.util.values.api.Value;

/**
 * @author g-litchfield
 * 
 */
public class MockTaker<V extends Value<V>> implements MarketTaker<V> {

	private final MarketField<MarketTrade> field;
	private final MarketEvent[] events;
	private final MarketInstrument[] insts;

	public MockTaker(final MarketInstrument[] insts) {
		field = MarketField.TRADE;
		events = new MarketEvent[] { MarketEvent.NEW_TRADE };
		this.insts = insts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.barchart.feed.base.market.api.MarketTaker#bindField()
	 */
	@Override
	public MarketField<V> bindField() {
		return (MarketField<V>) field;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.barchart.feed.base.market.api.MarketTaker#bindEvents()
	 */
	@Override
	public MarketEvent[] bindEvents() {
		return events;
	}

	@Override
	public MarketInstrument[] bindInstruments() {
		return insts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.market.api.MarketTaker#onMarketEvent(com.barchart
	 * .feed.base.market.enums.MarketEvent,
	 * com.barchart.feed.base.instrument.values.MarketInstrument,
	 * com.barchart.util.values.api.Value)
	 */
	@Override
	public void onMarketEvent(final MarketEvent event,
			final MarketInstrument instrument, final V value) {

	}

}
