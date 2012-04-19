/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import static com.barchart.feed.base.market.enums.MarketBarType.CURRENT;
import static com.barchart.feed.base.market.enums.MarketBarType.CURRENT_NET;
import static com.barchart.feed.base.market.enums.MarketBarType.CURRENT_PIT;
import static com.barchart.feed.base.market.enums.MarketField.CUVOL;
import static com.barchart.feed.base.market.enums.MarketField.CUVOL_LAST;
import static com.barchart.feed.base.market.enums.MarketField.MARKET;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.instrument.MockService;
import com.barchart.feed.base.instrument.api.DefinitionService;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.values.Market;
import com.barchart.feed.base.market.values.MarketCuvol;
import com.barchart.feed.base.market.values.MarketCuvolEntry;
import com.barchart.feed.base.message.MockMsgTrade;
import com.barchart.util.values.api.SizeValue;

public class TestProcessCuvol {

	DefinitionService service;

	private static final Logger log = LoggerFactory
			.getLogger(TestProcessCuvol.class);

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

		final MarketInstrument inst = service.lookup(newText("3"));

		final MarketInstrument[] insts = new MarketInstrument[] { inst };

		final MarketTaker<Market> tempTaker = new MockTaker<Market>(insts);

		maker.register(tempTaker);
		assertEquals(maker.marketCount(), 1);

		//

		Market market;
		MockMsgTrade msgTrade;
		MarketCuvol cuvol;
		SizeValue[] entries;
		MarketCuvolEntry entry;

		//

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_NET;
		msgTrade.price = newPrice(100000, -3);
		msgTrade.size = newSize(10);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);

		cuvol = market.get(CUVOL);
		// log.info("cuvol \n{}\n", cuvol);

		entries = cuvol.entries();
		assertEquals(entries.length, 1);
		assertEquals(entries[0], newSize(10));

		entry = market.get(CUVOL_LAST);
		assertEquals(entry.place(), 0);
		assertEquals(entry.price(), newPrice(100, 0));
		assertEquals(entry.size(), newSize(10));

		//

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_PIT;
		msgTrade.price = newPrice(100000, -3);
		msgTrade.size = newSize(10);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);
		cuvol = market.get(CUVOL);
		entries = cuvol.entries();
		// log.info("cuvol \n{}\n", cuvol);

		assertEquals(entries.length, 1);
		assertEquals(entries[0], newSize(20));

		//

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_PIT;
		msgTrade.price = newPrice(100375, -3);
		msgTrade.size = newSize(7);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);
		cuvol = market.get(CUVOL);
		entries = cuvol.entries();

		// log.info("cuvol \n{}\n", cuvol);

		assertEquals(entries.length, 4);
		assertEquals(entries[0], newSize(20));
		assertEquals(entries[1], newSize(0));
		assertEquals(entries[2], newSize(0));
		assertEquals(entries[3], newSize(7));

		msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT;
		msgTrade.price = newPrice(100375, -3);
		msgTrade.size = newSize(13);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		//

		market = maker.take(inst, MARKET);
		cuvol = market.get(CUVOL);
		entries = cuvol.entries();

		// log.info("cuvol \n{}\n", cuvol);

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
