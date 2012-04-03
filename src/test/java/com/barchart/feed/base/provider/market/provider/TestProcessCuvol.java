/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import static com.barchart.feed.base.api.market.enums.MarketBarType.*;
import static com.barchart.feed.base.api.market.enums.MarketField.*;
import static com.barchart.util.values.provider.ValueBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.api.instrument.DefinitionService;
import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.values.Market;
import com.barchart.feed.base.api.market.values.MarketCuvol;
import com.barchart.feed.base.api.market.values.MarketCuvolEntry;
import com.barchart.feed.base.mock.MockMsgTrade;
import com.barchart.feed.base.provider.instrument.provider.MockService;
import com.barchart.feed.base.provider.market.provider.MarketType;
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

		MockMaker maker = new MockMaker(MarketType.DDF);

		MarketInstrument inst = service.lookup(newText("3"));

		maker.register(inst);
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
