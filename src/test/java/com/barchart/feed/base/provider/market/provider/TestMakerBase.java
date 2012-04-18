/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import static com.barchart.util.values.provider.ValueBuilder.newText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.api.instrument.DefinitionService;
import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.MarketTaker;
import com.barchart.feed.base.api.market.enums.MarketEvent;
import com.barchart.feed.base.api.market.enums.MarketField;
import com.barchart.feed.base.api.market.values.Market;
import com.barchart.feed.base.provider.instrument.provider.MockService;

public class TestMakerBase {

	DefinitionService service;

	@Before
	public void setUp() throws Exception {
		service = new MockService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRegisterMarketTaker() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final MarketInstrument inst;

		inst = service.lookup(newText("1"));

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		MarketTaker<Market> taker;
		boolean isAdded;

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return null;
			}

			@Override
			public MarketField<Market> bindField() {
				return null;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] {};
			}

			@Override
			public MarketField<Market> bindField() {
				return null;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return null;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] {};
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] { inst };
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertTrue(isAdded);

	}

	@Test
	public void testRegisterMarketInstrument() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		MarketInstrument inst;

		inst = service.lookup(newText("1"));

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		//

		maker.unregister(inst);
		assertEquals(maker.marketCount(), 0);

		maker.unregister(inst);
		assertEquals(maker.marketCount(), 0);

	}

}
