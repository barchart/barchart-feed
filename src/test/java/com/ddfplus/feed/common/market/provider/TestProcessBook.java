package com.ddfplus.feed.common.market.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static com.ddfplus.feed.api.market.enums.MarketBarField.*;
import static com.ddfplus.feed.api.market.enums.MarketBarType.*;
import static com.ddfplus.feed.api.market.enums.MarketBookAction.*;
import static com.ddfplus.feed.api.market.enums.MarketBookSide.*;
import static com.ddfplus.feed.api.market.enums.MarketBookType.*;
import static com.ddfplus.feed.api.market.enums.MarketEvent.*;
import static com.ddfplus.feed.api.market.enums.MarketField.*;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.api.SizeValue;
import com.ddfplus.feed.api.instrument.DefinitionService;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.MarketTaker;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.values.Market;
import com.ddfplus.feed.api.market.values.MarketBar;
import com.ddfplus.feed.api.market.values.MarketBook;
import com.ddfplus.feed.api.market.values.MarketBookEntry;
import com.ddfplus.feed.api.market.values.MarketBookTop;
import com.ddfplus.feed.api.market.values.MarketTrade;
import com.ddfplus.feed.common.instrument.provider.MockService;
import com.ddfplus.market.impl.mock.MockMsgBook;
import com.ddfplus.market.impl.mock.MockMsgTrade;

public class TestProcessBook {

	DefinitionService service;

	private static final Logger log = LoggerFactory
			.getLogger(TestProcessBook.class);

	@Before
	public void setUp() throws Exception {
		service = new MockService();
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

		final MockMaker maker = new MockMaker(MarketType.DDF);

		MarketInstrument inst;

		inst = service.lookup(newText("2"));

		final boolean isRegistered = maker.register(inst);
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

		final MockMaker maker = new MockMaker(MarketType.DDF);

		final MarketInstrument inst;

		inst = service.lookup(newText("2"));

		maker.register(inst);
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
		msgTrade.type = CURRENT_NET;
		msgTrade.price = newPrice(100, 0);
		msgTrade.size = newSize(17);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		assertEquals(count.get(), 0);

	}

}
