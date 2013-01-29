/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import static com.barchart.feed.base.bar.enums.MarketBarField.BAR_TIME;
import static com.barchart.feed.base.bar.enums.MarketBarField.VOLUME;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT;
import static com.barchart.feed.base.market.enums.MarketField.MARKET;
import static com.barchart.feed.base.market.enums.MarketField.TRADE;
import static com.barchart.feed.base.trade.enums.MarketTradeField.PRICE;
import static com.barchart.feed.base.trade.enums.MarketTradeField.SIZE;
import static com.barchart.feed.base.trade.enums.MarketTradeField.TRADE_TIME;
import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.NORMAL;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.DEFAULT;
import static com.barchart.feed.base.trade.enums.MarketTradeType.FUTURE_ELECTRONIC;
import static com.barchart.feed.base.trade.enums.MarketTradeType.FUTURE_PIT;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.message.MockMsgTrade;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentService;

public class TestProcessBar {

	InstrumentService service;

	private static final Logger log = LoggerFactory
			.getLogger(TestProcessBar.class);

	@Before
	public void setUp() throws Exception {
		service = new MockDefinitionService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTakerMarket() {

		Market market;
		MarketBar bar;
		MarketTrade trade;

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		Instrument inst;

		inst = service.lookup(MockDefinitionService.INST_SYMBOL_1);

		final Instrument[] insts = new Instrument[] { inst };

		final MarketTaker<Market> tempTaker = new MockTaker<Market>(insts);

		final boolean isRegistered = maker.register(tempTaker);
		assertTrue(isRegistered);
		assertEquals(maker.marketCount(), 1);

		market = maker.take(inst, MARKET);
		assertFalse(market.isNull());

		trade = market.get(TRADE);
		assertTrue(trade.isNull());

		//

		final MockMsgTrade msgTrade = new MockMsgTrade(inst);

		//

		msgTrade.type = FUTURE_ELECTRONIC;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(1230, -1);
		msgTrade.size = newSize(17);
		msgTrade.time = newTime(345679);
		msgTrade.date = newTime(345670);

		maker.make(msgTrade);
		market = maker.take(inst, MARKET);

		//

		trade = market.get(TRADE);
		assertFalse(trade.isNull());

		assertEquals(trade.get(PRICE), newPrice(1230, -1));
		assertEquals(trade.get(SIZE), newSize(17));
		assertEquals(trade.get(TRADE_TIME), newTime(345679));

		//

		bar = market.get(BAR_CURRENT);
		assertEquals(bar.get(VOLUME), newSize(17));
		// assertEquals(bar.get(HIGH), newPrice(1230, -1));
		// assertEquals(bar.get(LOW), newPrice(1230, -1));
		assertEquals(bar.get(BAR_TIME), newTime(345679));

		//

		msgTrade.type = FUTURE_PIT;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(1230, -1);
		msgTrade.size = newSize(13);
		msgTrade.time = newTime(345678); // past trade
		msgTrade.date = newTime(345670); // past trade

		maker.make(msgTrade);

		//

		msgTrade.type = FUTURE_PIT;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(1240, -1);
		msgTrade.size = newSize(13);
		msgTrade.time = newTime(345680); // next trade
		msgTrade.date = newTime(345670); // next trade

		maker.make(msgTrade);
		market = maker.take(inst, MARKET); // updated market

		bar = market.get(BAR_CURRENT);
		assertFalse(bar.isNull());
		// assertEquals(bar.get(VOLUME), newSize(30));
		// assertEquals(bar.get(HIGH), newPrice(124000, -3));
		// assertEquals(bar.get(LOW), newPrice(123, 0));
		// assertEquals(bar.get(BAR_TIME), newTime(345680));

		//

		msgTrade.type = FUTURE_ELECTRONIC;
		msgTrade.session = DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(1200, -1);
		msgTrade.size = newSize(111);
		msgTrade.time = newTime(345700);
		msgTrade.date = newTime(345670); // next trade

		maker.make(msgTrade);
		market = maker.take(inst, MARKET); // updated market

		// updated
		bar = market.get(BAR_CURRENT);
		assertFalse(bar.isNull());
		// assertEquals(bar.get(VOLUME), newSize(141));
		// assertEquals(bar.get(HIGH), newPrice(124000, -3));
		// assertEquals(bar.get(LOW), newPrice(1200, -1));
		// assertEquals(bar.get(BAR_TIME), newTime(345700));

		//

	}
}
