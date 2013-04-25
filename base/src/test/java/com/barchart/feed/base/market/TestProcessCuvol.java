/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import static com.barchart.feed.base.market.enums.MarketField.CUVOL;
import static com.barchart.feed.base.market.enums.MarketField.CUVOL_LAST;
import static com.barchart.feed.base.market.enums.MarketField.MARKET;
import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.NORMAL;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.DEFAULT;
import static com.barchart.feed.base.trade.enums.MarketTradeType.FUTURE_ELECTRONIC;
import static com.barchart.feed.base.trade.enums.MarketTradeType.FUTURE_PIT;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.inst.InstrumentService;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.message.MockMsgTrade;
import com.barchart.feed.inst.missive.BarchartFeedInstManifest;
import com.barchart.feed.inst.provider.MockDefinitionService;
import com.barchart.missive.core.ObjectMapFactory;
import com.barchart.util.values.api.SizeValue;

public class TestProcessCuvol {

	InstrumentService<CharSequence> service;

	private static final Logger log = LoggerFactory
			.getLogger(TestProcessCuvol.class);

	@Before
	public void setUp() throws Exception {
		ObjectMapFactory.install(new BarchartFeedInstManifest());
		service = new MockDefinitionService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTakerMarket() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final Instrument inst = service.lookup(MockDefinitionService.INST_SYMBOL_3);

		final Instrument[] insts = new Instrument[] { inst };

		final MarketTaker<Market> tempTaker = new MockTaker<Market>(insts);

		assertTrue(maker.register(tempTaker));
		assertEquals(maker.marketCount(), 1);

		//

		Market market;
		MockMsgTrade msgTrade;
		MarketCuvol cuvol;
		SizeValue[] entries;
		MarketCuvolEntry entry;

		//

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = FUTURE_ELECTRONIC;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(100000, -3);
		msgTrade.size = newSize(10);
		msgTrade.time = newTime(0);
		msgTrade.date = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);

		cuvol = market.get(CUVOL);

		entries = cuvol.entries();
		assertEquals(entries.length, 1);
		assertEquals(entries[0], newSize(10));

		entry = market.get(CUVOL_LAST);
		assertEquals(entry.place(), 0);
		assertEquals(entry.price(), newPrice(100, 0));
		assertEquals(entry.size(), newSize(10));

		//

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = FUTURE_PIT;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(100000, -3);
		msgTrade.size = newSize(10);
		msgTrade.time = newTime(0);
		msgTrade.date = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);
		cuvol = market.get(CUVOL);
		entries = cuvol.entries();

		assertEquals(entries.length, 1);
		assertEquals(entries[0], newSize(20));

		//

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = FUTURE_PIT;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(100375, -3);
		msgTrade.size = newSize(7);
		msgTrade.time = newTime(0);
		msgTrade.date = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);
		cuvol = market.get(CUVOL);
		entries = cuvol.entries();

		assertEquals(entries.length, 4);
		assertEquals(entries[0], newSize(20));
		assertEquals(entries[1], newSize(0));
		assertEquals(entries[2], newSize(0));
		assertEquals(entries[3], newSize(7));

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = FUTURE_ELECTRONIC;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(100375, -3);
		msgTrade.size = newSize(13);
		msgTrade.time = newTime(0);
		msgTrade.date = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);
		cuvol = market.get(CUVOL);
		entries = cuvol.entries();

		assertEquals(entries.length, 4);
		assertEquals(entries[0], newSize(20));
		assertEquals(entries[1], newSize(0));
		assertEquals(entries[2], newSize(0));
		assertEquals(entries[3], newSize(20));

		//

		entry = market.get(CUVOL_LAST);
		assertEquals(entry.place(), 3);
		assertEquals(entry.price(), newPrice(100375, -3));
		assertEquals(entry.size(), newSize(20));

	}

}
