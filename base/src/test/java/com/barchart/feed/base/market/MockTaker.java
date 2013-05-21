/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
/**
 * 
 */
package com.barchart.feed.base.market;

import com.barchart.feed.api.data.InstrumentEntity;
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
	private final InstrumentEntity[] insts;

	public MockTaker(final InstrumentEntity[] insts) {
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
	public InstrumentEntity[] bindInstruments() {
		return insts;
	}

	@Override
	public void onMarketEvent(final MarketEvent event,
			final InstrumentEntity instrument, final Market value) {

	}

}
