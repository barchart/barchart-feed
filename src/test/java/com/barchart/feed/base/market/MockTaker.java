/**
 * 
 */
package com.barchart.feed.base.market;

import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;

/**
 * @author g-litchfield
 * 
 */
public class MockTaker<V> implements MarketTaker<Market> {

	private final MarketField<Market> field;
	private final MarketEvent[] events;
	private final MarketInstrument[] insts;

	public MockTaker(final MarketInstrument[] insts) {
		field = MarketField.MARKET;
		events = new MarketEvent[] { MarketEvent.NEW_TRADE };
		this.insts = insts;
	}

	@Override
	public MarketField<Market> bindField() {
		return field;
	}

	@Override
	public MarketEvent[] bindEvents() {
		return events;
	}

	@Override
	public MarketInstrument[] bindInstruments() {
		return insts;
	}

	@Override
	public void onMarketEvent(final MarketEvent event,
			final MarketInstrument instrument, final Market value) {

	}

}
