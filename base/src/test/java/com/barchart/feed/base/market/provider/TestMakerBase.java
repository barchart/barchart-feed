/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.inst.InstrumentService;
import com.barchart.feed.base.market.MockMaker;
import com.barchart.feed.base.market.MockMarketFactory;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.inst.missive.BarchartFeedInstManifest;
import com.barchart.feed.inst.provider.MockDefinitionService;
import com.barchart.missive.core.ObjectMapFactory;

public class TestMakerBase {

	InstrumentService<CharSequence> service;

	@Before
	public void setUp() throws Exception {
		ObjectMapFactory.install(new BarchartFeedInstManifest());
		service = new MockDefinitionService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRegisterMarketTaker() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final Instrument inst;

		inst = service.lookup(MockDefinitionService.INST_SYMBOL_1);

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		{

			final MarketTaker<Market> taker = new MarketTaker<Market>() {
				@Override
				public MarketEvent[] bindEvents() {
					return null;
				}

				@Override
				public MarketField<Market> bindField() {
					return null;
				}

				@Override
				public Instrument[] bindInstruments() {
					return null;
				}

				@Override
				public void onMarketEvent(final MarketEvent event,
						final Instrument instrument, final Market value) {
				}
			};
			final boolean isAdded = maker.register(taker);
			assertFalse("should not register invalid taker", isAdded);

		}
		{

			final MarketTaker<Market> taker = new MarketTaker<Market>() {
				@Override
				public MarketEvent[] bindEvents() {
					return new MarketEvent[] {};
				}

				@Override
				public MarketField<Market> bindField() {
					return null;
				}

				@Override
				public Instrument[] bindInstruments() {
					return null;
				}

				@Override
				public void onMarketEvent(final MarketEvent event,
						final Instrument instrument, final Market value) {
				}
			};
			final boolean isAdded = maker.register(taker);
			assertFalse("should not register invalid taker", isAdded);

		}

		{

			final MarketTaker<Market> taker = new MarketTaker<Market>() {
				@Override
				public MarketEvent[] bindEvents() {
					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				}

				@Override
				public MarketField<Market> bindField() {
					return null;
				}

				@Override
				public Instrument[] bindInstruments() {
					return null;
				}

				@Override
				public void onMarketEvent(final MarketEvent event,
						final Instrument instrument, final Market value) {
				}
			};
			final boolean isAdded = maker.register(taker);
			assertFalse(isAdded);

		}

		{
			final MarketTaker<Market> taker = new MarketTaker<Market>() {
				@Override
				public MarketEvent[] bindEvents() {
					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				}

				@Override
				public MarketField<Market> bindField() {
					return MarketField.MARKET;
				}

				@Override
				public Instrument[] bindInstruments() {
					return null;
				}

				@Override
				public void onMarketEvent(final MarketEvent event,
						final Instrument instrument, final Market value) {
				}
			};
			final boolean isAdded = maker.register(taker);
			assertFalse("should not register invalid taker", isAdded);

		}

		{
			final MarketTaker<Market> taker = new MarketTaker<Market>() {
				@Override
				public MarketEvent[] bindEvents() {
					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				}

				@Override
				public MarketField<Market> bindField() {
					return MarketField.MARKET;
				}

				@Override
				public Instrument[] bindInstruments() {
					return new Instrument[] {};
				}

				@Override
				public void onMarketEvent(final MarketEvent event,
						final Instrument instrument, final Market value) {
				}
			};
			final boolean isAdded = maker.register(taker);
			assertTrue("ok to have non null but empty fields", isAdded);

		}

		{
			final MarketTaker<Market> taker = new MarketTaker<Market>() {
				@Override
				public MarketEvent[] bindEvents() {
					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				}

				@Override
				public MarketField<Market> bindField() {
					return MarketField.MARKET;
				}

				@Override
				public Instrument[] bindInstruments() {
					return new Instrument[] { inst };
				}

				@Override
				public void onMarketEvent(final MarketEvent event,
						final Instrument instrument, final Market value) {
				}
			};
			final boolean isAdded = maker.register(taker);
			assertTrue(isAdded);
		}

	}

	@Test
	public void testRegisterMarketInstrument() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		Instrument inst;

		inst = service.lookup(MockDefinitionService.INST_SYMBOL_1);

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
