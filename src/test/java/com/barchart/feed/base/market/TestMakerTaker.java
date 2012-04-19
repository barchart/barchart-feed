/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import static com.barchart.feed.base.market.enums.MarketBarField.VOLUME;
import static com.barchart.feed.base.market.enums.MarketBarType.CURRENT_NET;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.instrument.MockService;
import com.barchart.feed.base.instrument.api.DefinitionService;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.market.values.Market;
import com.barchart.feed.base.market.values.MarketBar;
import com.barchart.feed.base.message.MockMsgTrade;
import com.barchart.util.values.api.SizeValue;

public class TestMakerTaker {

	DefinitionService service;

	private static final Logger log = LoggerFactory
			.getLogger(TestMakerTaker.class);

	@Before
	public void setUp() throws Exception {
		service = new MockService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTakerMarket() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		// final MockTaker taker = new MockTaker();

		final MarketInstrument inst;

		inst = service.lookup(newText("1"));

		final MarketInstrument[] insts = new MarketInstrument[] { inst };

		final MarketTaker<Market> tempTaker = new MockTaker<Market>(insts);

		maker.register(tempTaker);
		assertEquals(maker.marketCount(), 1);

		MarketTaker<Market> taker;
		boolean isAdded;

		final AtomicInteger count = new AtomicInteger(0);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				// return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				return MarketEvent.values();
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] { inst };
			}

			Market previous;

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market market) {

				count.incrementAndGet();

				log.info("event : {}  market : {}", event, market.hashCode());
				log.info("BAR_CURRENT = " + market.get(BAR_CURRENT).get(VOLUME));

				if (previous == null) {
					previous = market;
				}

				assertTrue(previous == market);
				previous = market;

				assertEquals(inst, instrument);

				final SizeValue volume = market.get(BAR_CURRENT).get(VOLUME);

				assertEquals(volume, newSize(10));

			}
		};
		isAdded = maker.register(taker);
		assertTrue(isAdded);
		assertEquals(maker.marketCount(), 1);

		//

		final MockMsgTrade msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_NET;
		msgTrade.price = newPrice(100, 0);
		msgTrade.size = newSize(10);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		assertEquals(count.get(), 6);

	}

	@Test
	public void testTakerBar() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final MarketInstrument inst;

		inst = service.lookup(newText("1"));

		final MarketInstrument[] insts = new MarketInstrument[] { inst };

		final MarketTaker<Market> tempTaker = new MockTaker<Market>(insts);

		maker.register(tempTaker);
		assertEquals(maker.marketCount(), 1);

		MarketTaker<MarketBar> taker;
		boolean isAdded;

		final AtomicInteger count = new AtomicInteger(0);

		taker = new MarketTaker<MarketBar>() {
			@Override
			public MarketEvent[] bindEvents() {
				// return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				return MarketEvent.values();
			}

			@Override
			public MarketField<MarketBar> bindField() {
				return BAR_CURRENT;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] { inst };
			}

			MarketBar previous;

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final MarketBar bar) {

				count.incrementAndGet();

				log.info("event : {}  bar : {}", event, bar.hashCode());

				if (previous == null) {
					previous = bar;
				}

				assertTrue(previous == bar);
				previous = bar;

				assertEquals(inst, instrument);

				final SizeValue volume = bar.get(VOLUME);

				assertEquals(volume, newSize(17));

			}
		};
		isAdded = maker.register(taker);
		assertTrue(isAdded);
		assertEquals(maker.marketCount(), 1);

		//

		final MockMsgTrade msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_NET;
		msgTrade.price = newPrice(100, 0);
		msgTrade.size = newSize(17);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		assertEquals(count.get(), 6);

	}

}
