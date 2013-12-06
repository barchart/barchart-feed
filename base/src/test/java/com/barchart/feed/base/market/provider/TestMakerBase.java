/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import org.junit.After;
import org.junit.Before;

import com.barchart.feed.inst.InstrumentService;

public class TestMakerBase {

	InstrumentService<CharSequence> service;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testRegisterMarketTaker() {
//
//		final MockMaker maker = new MockMaker(new MockMarketFactory());
//
//		final Instrument inst;
//
//		inst = service.lookup(MockDefinitionService.INST_SYMBOL_1);
//
//		maker.register(inst);
//		assertEquals(maker.marketCount(), 1);
//
//		{
//
//			final MarketTaker<Market> taker = new MarketTaker<Market>() {
//				@Override
//				public MarketEvent[] bindEvents() {
//					return null;
//				}
//
//				@Override
//				public MarketField<Market> bindField() {
//					return null;
//				}
//
//				@Override
//				public Instrument[] bindInstruments() {
//					return null;
//				}
//
//				@Override
//				public void onMarketEvent(final MarketEvent event,
//						final Instrument instrument, final Market value) {
//				}
//			};
//			final boolean isAdded = maker.register(taker);
//			assertFalse("should not register invalid taker", isAdded);
//
//		}
//		{
//
//			final MarketTaker<Market> taker = new MarketTaker<Market>() {
//				@Override
//				public MarketEvent[] bindEvents() {
//					return new MarketEvent[] {};
//				}
//
//				@Override
//				public MarketField<Market> bindField() {
//					return null;
//				}
//
//				@Override
//				public Instrument[] bindInstruments() {
//					return null;
//				}
//
//				@Override
//				public void onMarketEvent(final MarketEvent event,
//						final Instrument instrument, final Market value) {
//				}
//			};
//			final boolean isAdded = maker.register(taker);
//			assertFalse("should not register invalid taker", isAdded);
//
//		}
//
//		{
//
//			final MarketTaker<Market> taker = new MarketTaker<Market>() {
//				@Override
//				public MarketEvent[] bindEvents() {
//					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
//				}
//
//				@Override
//				public MarketField<Market> bindField() {
//					return null;
//				}
//
//				@Override
//				public Instrument[] bindInstruments() {
//					return null;
//				}
//
//				@Override
//				public void onMarketEvent(final MarketEvent event,
//						final Instrument instrument, final Market value) {
//				}
//			};
//			final boolean isAdded = maker.register(taker);
//			assertFalse(isAdded);
//
//		}
//
//		{
//			final MarketTaker<Market> taker = new MarketTaker<Market>() {
//				@Override
//				public MarketEvent[] bindEvents() {
//					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
//				}
//
//				@Override
//				public MarketField<Market> bindField() {
//					return MarketField.MARKET;
//				}
//
//				@Override
//				public Instrument[] bindInstruments() {
//					return null;
//				}
//
//				@Override
//				public void onMarketEvent(final MarketEvent event,
//						final Instrument instrument, final Market value) {
//				}
//			};
//			final boolean isAdded = maker.register(taker);
//			assertFalse("should not register invalid taker", isAdded);
//
//		}
//
//		{
//			final MarketTaker<Market> taker = new MarketTaker<Market>() {
//				@Override
//				public MarketEvent[] bindEvents() {
//					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
//				}
//
//				@Override
//				public MarketField<Market> bindField() {
//					return MarketField.MARKET;
//				}
//
//				@Override
//				public Instrument[] bindInstruments() {
//					return new Instrument[] {};
//				}
//
//				@Override
//				public void onMarketEvent(final MarketEvent event,
//						final Instrument instrument, final Market value) {
//				}
//			};
//			final boolean isAdded = maker.register(taker);
//			assertTrue("ok to have non null but empty fields", isAdded);
//
//		}
//
//		{
//			final MarketTaker<Market> taker = new MarketTaker<Market>() {
//				@Override
//				public MarketEvent[] bindEvents() {
//					return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
//				}
//
//				@Override
//				public MarketField<Market> bindField() {
//					return MarketField.MARKET;
//				}
//
//				@Override
//				public Instrument[] bindInstruments() {
//					return new Instrument[] { inst };
//				}
//
//				@Override
//				public void onMarketEvent(final MarketEvent event,
//						final Instrument instrument, final Market value) {
//				}
//			};
//			final boolean isAdded = maker.register(taker);
//			assertTrue(isAdded);
//		}
//
//	}
//
//	@Test
//	public void testRegisterMarketInstrument() {
//
//		final MockMaker maker = new MockMaker(new MockMarketFactory());
//
//		Instrument inst;
//
//		inst = service.lookup(MockDefinitionService.INST_SYMBOL_1);
//
//		maker.register(inst);
//		assertEquals(maker.marketCount(), 1);
//
//		maker.register(inst);
//		assertEquals(maker.marketCount(), 1);
//
//		maker.register(inst);
//		assertEquals(maker.marketCount(), 1);
//
//		//
//
//		maker.unregister(inst);
//		assertEquals(maker.marketCount(), 0);
//
//		maker.unregister(inst);
//		assertEquals(maker.marketCount(), 0);
//
//	}

}
