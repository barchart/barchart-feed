/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import static com.barchart.feed.base.bar.enums.MarketBarField.VOLUME;
import static com.barchart.feed.base.book.enums.MarketBookAction.MODIFY;
import static com.barchart.feed.base.book.enums.MarketBookAction.REMOVE;
import static com.barchart.feed.base.book.enums.MarketBookSide.ASK;
import static com.barchart.feed.base.book.enums.MarketBookSide.BID;
import static com.barchart.feed.base.book.enums.MarketBookType.DEFAULT;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BOOK_TOP;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BOOK_UPDATE;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT;
import static com.barchart.feed.base.market.enums.MarketField.BOOK;
import static com.barchart.feed.base.market.enums.MarketField.BOOK_TOP;
import static com.barchart.feed.base.market.enums.MarketField.MARKET;
import static com.barchart.feed.base.market.enums.MarketField.TRADE;
import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.NORMAL;
import static com.barchart.feed.base.trade.enums.MarketTradeType.FUTURE_ELECTRONIC;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newSize;
import static com.barchart.util.values.provider.ValueBuilder.newText;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.instrument.MockDefinitionService;
import com.barchart.feed.base.instrument.api.DefinitionService;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.message.MockMsgBook;
import com.barchart.feed.base.message.MockMsgTrade;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.util.values.api.SizeValue;

public class TestProcessBook {

	DefinitionService service;

	private static final Logger log = LoggerFactory
			.getLogger(TestProcessBook.class);

	@Before
	public void setUp() throws Exception {
		service = new MockDefinitionService();
	}

	@After
	public void tearDown() throws Exception {
	}

	// TODO
	// @Test
	public void testBookMake() {

		Market market;
		MarketBook book;
		MarketBookTop top;
		MarketBookEntry entry;
		MarketBookEntry[] bids;
		MarketBookEntry[] asks;
		MarketTrade trade;

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		MarketInstrument inst;

		inst = service.lookup(newText("2"));

		final MarketInstrument[] insts = new MarketInstrument[] { inst };

		final MarketTaker<Market> tempTaker = new MockTaker<Market>(insts);

		final boolean isRegistered = maker.register(tempTaker);
		assertTrue(isRegistered);
		assertEquals(maker.marketCount(), 1);

		market = maker.take(inst, MARKET);
		assertFalse(market.isNull());

		trade = market.get(TRADE);
		assertTrue(trade.isNull());

		book = market.get(BOOK); // null book
		// System.out.println(book);
		assertEquals(1, book.entries(BID).length);
		assertEquals(1, book.entries(ASK).length);

		//

		final MockMsgBook msgBook = new MockMsgBook(inst);

		//

		msgBook.act = MODIFY;
		msgBook.side = BID;
		msgBook.type = DEFAULT;
		msgBook.place = 0;
		msgBook.price = newPrice(1000, -2);
		msgBook.size = newSize(11);
		msgBook.time = newTime(123);

		maker.make(msgBook);
		market = maker.take(inst, MARKET);
		assertTrue(market.isFrozen());

		book = market.get(BOOK);
		// System.out.println(book);
		assertTrue(book.isFrozen());
		assertEquals(newTime(123), book.time());

		bids = book.entries(BID);
		assertEquals(1, bids.length);

		top = market.get(BOOK_TOP);
		assertTrue(top.isFrozen());
		entry = top.side(BID);
		assertEquals(1, entry.place());
		assertEquals(newPrice(1000, -2), entry.price());
		assertEquals(newSize(11), entry.size());

		//

		msgBook.act = MODIFY;
		msgBook.side = BID;
		msgBook.type = DEFAULT;
		msgBook.place = 0;
		msgBook.price = newPrice(1100, -2);
		msgBook.size = newSize(13);
		msgBook.time = newTime(125);

		maker.make(msgBook);
		market = maker.take(inst, MARKET);
		book = market.get(BOOK);
		// System.out.println(book);

		bids = book.entries(BID);

		assertEquals(2, bids.length);
		assertEquals(newTime(125), book.time());

		//

		msgBook.act = MODIFY;
		msgBook.side = ASK;
		msgBook.type = DEFAULT;
		msgBook.place = 0;
		msgBook.price = newPrice(1225, -2);
		msgBook.size = newSize(15);
		msgBook.time = newTime(127);

		maker.make(msgBook);
		market = maker.take(inst, MARKET);
		book = market.get(BOOK);
		// System.out.println(book);

		top = market.get(BOOK_TOP);
		entry = top.side(ASK);
		assertEquals(1, entry.place());
		assertEquals(newPrice(1225, -2), entry.price());
		assertEquals(newSize(15), entry.size());

		//

		msgBook.act = MODIFY;
		msgBook.side = ASK;
		msgBook.type = DEFAULT;
		msgBook.place = 0;
		msgBook.price = newPrice(1300, -2);
		msgBook.size = newSize(17);
		msgBook.time = newTime(129);

		maker.make(msgBook);
		market = maker.take(inst, MARKET);
		book = market.get(BOOK);
		System.out.println(book);

		//

		bids = book.entries(BID);

		entry = bids[0];
		assertEquals(2, entry.place());
		assertEquals(newPrice(1000, -2), entry.price());
		assertEquals(newSize(11), entry.size());

		entry = bids[1];
		assertEquals(1, entry.place());
		assertEquals(newPrice(1100, -2), entry.price());
		assertEquals(newSize(13), entry.size());

		//

		asks = book.entries(ASK);

		entry = asks[0];
		assertEquals(1, entry.place());
		assertEquals(newPrice(1225, -2), entry.price());
		assertEquals(newSize(15), entry.size());

		entry = asks[1];
		assertEquals(2, entry.place());
		assertEquals(newPrice(1300, -2), entry.price());
		assertEquals(newSize(17), entry.size());

		//

		msgBook.act = REMOVE;
		msgBook.side = ASK;
		msgBook.type = DEFAULT;
		msgBook.place = 1;
		msgBook.price = null;
		msgBook.size = null;
		msgBook.time = newTime(131);

		maker.make(msgBook);
		market = maker.take(inst, MARKET);
		book = market.get(BOOK);
		System.out.println(book);

		top = market.get(BOOK_TOP);
		entry = top.side(ASK);
		assertEquals(1, entry.place());
		assertEquals(newPrice(1300, -2), entry.price());
		assertEquals(newSize(17), entry.size());

		//

		msgBook.act = REMOVE;
		msgBook.side = BID;
		msgBook.type = DEFAULT;
		msgBook.place = 0;
		msgBook.price = newPrice(1200, -2);
		msgBook.size = null;
		msgBook.time = newTime(131);

		maker.make(msgBook);
		market = maker.take(inst, MARKET);
		book = market.get(BOOK);
		System.out.println(book);

		top = market.get(BOOK_TOP);
		entry = top.side(BID);
		assertEquals(1, entry.place());
		assertEquals(newPrice(1100, -2), entry.price());
		assertEquals(newSize(13), entry.size());

	}

	@Test
	public void testBookTake() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final MarketInstrument inst;

		inst = service.lookup(newText("2"));

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
				return new MarketEvent[] { NEW_BOOK_UPDATE, NEW_BOOK_TOP };
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
		msgTrade.type = FUTURE_ELECTRONIC;
		msgTrade.session = MarketTradeSession.DEFAULT;
		msgTrade.sequencing = NORMAL;
		msgTrade.price = newPrice(100, 0);
		msgTrade.size = newSize(17);
		msgTrade.time = newTime(0);
		msgTrade.date = newTime(0);

		maker.make(msgTrade);

		assertEquals(count.get(), 0);

	}

}
