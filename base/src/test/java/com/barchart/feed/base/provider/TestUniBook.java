/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.book.enums.MarketBookAction.MODIFY;
import static com.barchart.feed.base.book.enums.MarketBookAction.NOOP;
import static com.barchart.feed.base.book.enums.MarketBookAction.REMOVE;
import static com.barchart.feed.base.book.enums.UniBookResult.ERROR;
import static com.barchart.feed.base.book.enums.UniBookResult.NORMAL;
import static com.barchart.feed.base.book.enums.UniBookResult.TOP;
import static com.barchart.feed.base.values.provider.ValueBuilder.newPrice;
import static com.barchart.feed.base.values.provider.ValueBuilder.newSize;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_PRICE;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_SIZE;
import static com.barchart.feed.base.values.provider.ValueConst.ZERO_PRICE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.util.common.bench.JavaSize;

public class TestUniBook {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test()
	public void testNullEntryAddDelete() {
		final SizeValue size = newSize(10);
		final PriceValue priceStep = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL, 
				Book.Type.COMBINED, size, priceStep);
		UniBookResult result;
		result = book.make(null);
		assertEquals(result, ERROR);
		result = book.make(null);
		assertEquals(result, ERROR);
	}

	@Test()
	public void testWrongEnums() {

		final SizeValue size = newSize(10);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL, 
				Book.Type.COMBINED, size, step);
		DefBookEntry entry;
		UniBookResult result;

		entry = new DefBookEntry(
				NOOP, Book.Side.NULL, Book.Type.NONE, 0, newPrice(1000, -2),
				NULL_SIZE);
		result = book.make(entry);
		assertEquals(result, ERROR);

		entry = new DefBookEntry(
				MODIFY, Book.Side.NULL, Book.Type.NONE, 0, newPrice(1000, -2),
				NULL_SIZE);
		result = book.make(entry);
		assertEquals(result, ERROR);

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.NONE, 0, newPrice(1000, -2),
				NULL_SIZE);
		result = book.make(entry);
		assertEquals(result, ERROR);

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 0, newPrice(1000, -2),
				NULL_SIZE);
		result = book.make(entry);
		assertEquals(result, TOP);

	}

	@Test()
	public void testJavaSize1() {
		final SizeValue size = newSize(10);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.COMBINED, size, step);
		final int bookSize = JavaSize.of(book);
		assertEquals(bookSize, 304);
	}

	@Test()
	public void testJavaSize2() {
		final SizeValue size = newSize(10);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.DEFAULT, size, step);
		final int bookSize = JavaSize.of(book);
		assertEquals(bookSize, 208);
	}

	@Test()
	public void testJavaSize3() {
		final SizeValue size = newSize(10);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.NONE, size, step);
		final int bookSize = JavaSize.of(book);
		assertEquals(bookSize, 112);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConst0() {
		final SizeValue size = newSize(UniBookRing.PLACE_SIZE + 1); // invalid
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.COMBINED, size, step);
		System.out.println(book);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConst1() {
		final SizeValue size = newSize(10);
		final PriceValue step = ZERO_PRICE; // invalid
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.COMBINED, size, step);
		System.out.println(book);
	}

	@Test
	public void testBasicAddDelete() {
		//
		final SizeValue size = newSize(10);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.DEFAULT, size, step);
		final UniBookRing bids = book.bids;
		final UniBookRing asks = book.asks;
		MarketDoBookEntry entry;
		UniBookResult result;

		assertTrue(book.isEmpty(Book.Side.BID));
		assertTrue(book.isEmpty(Book.Side.ASK));

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 0, newPrice(1000, -2),
				newSize(13));
		result = book.make(entry);
		// System.out.println(book);
		entry = book.topFor(Book.Side.BID);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(1000, -2));
		assertEquals(entry.sizeValue(), newSize(13));
		assertEquals(result, TOP);
		assertEquals(bids.placeCount(), bids.count());

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 1, newPrice(1100, -2),
				newSize(17));
		result = book.make(entry);
		// System.out.println(book);
		entry = book.topFor(Book.Side.ASK);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(1100, -2));
		assertEquals(entry.sizeValue(), newSize(17));
		assertEquals(result, TOP);
		assertEquals(asks.placeCount(), asks.count());

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 0, newPrice(900, -2),
				newSize(9));
		result = book.make(entry);
		// System.out.println(book);
		entry = bids.get(newPrice(900, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry.place(), 2);
		assertEquals(entry.priceValue(), newPrice(900, -2));
		assertEquals(entry.sizeValue(), newSize(9));
		assertEquals(bids.placeCount(), bids.count());

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 0, newPrice(1200, -2),
				newSize(21));
		result = book.make(entry);
		// System.out.println(book);
		entry = asks.get(newPrice(1200, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry.place(), 2);
		assertEquals(entry.priceValue(), newPrice(1200, -2));
		assertEquals(entry.sizeValue(), newSize(21));
		assertEquals(asks.placeCount(), asks.count());

		//

		entry = new DefBookEntry(
				REMOVE, Book.Side.ASK, Book.Type.DEFAULT, 2, null, null);
		result = book.make(entry);
		// System.out.println(book);
		entry = asks.get(newPrice(1200, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry, null);
		assertEquals(asks.placeCount(), asks.count());

		//

		entry = new DefBookEntry(
				REMOVE, Book.Side.BID, Book.Type.DEFAULT, 1, null, null);
		result = book.make(entry);
		// System.out.println(book);
		entry = bids.get(newPrice(1000, -2));
		assertEquals(result, TOP);
		assertEquals(entry, null);
		assertEquals(bids.placeCount(), bids.count());

		//

		entry = new DefBookEntry(
				REMOVE, Book.Side.BID, Book.Type.DEFAULT, 1, null, null);
		result = book.make(entry);
		// System.out.println(book);
		entry = bids.get(newPrice(900, -2));
		assertEquals(result, TOP);
		assertEquals(entry, null);
		assertTrue(book.isEmpty(Book.Side.BID));
		assertEquals(bids.placeCount(), bids.count());

		//

		entry = new DefBookEntry(
				REMOVE, Book.Side.ASK, Book.Type.DEFAULT, 1, null, null);
		result = book.make(entry);
		// System.out.println(book);
		entry = asks.get(newPrice(1100, -2));
		assertEquals(result, TOP);
		assertEquals(entry, null);
		assertTrue(book.isEmpty(Book.Side.ASK));
		assertEquals(asks.placeCount(), asks.count());

	}

	@Test
	public void testCountPlaces() {

		final SizeValue size = newSize(16);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.COMBINED, size, step);
		final UniBookRing asks = book.asks;
		final UniBookRing bids = book.bids;
		UniBookResult result;
		MarketDoBookEntry entry;

		// ###

		assertTrue(book.isEmpty(Book.Side.ASK));

		assertEquals(0, asks.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 1, newPrice(1000, -2),
				newSize(13));
		book.make(entry);
		// System.out.println(book);

		assertEquals(1, asks.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 0, newPrice(1200, -2),
				newSize(15));
		book.make(entry);
		// System.out.println(book);

		assertEquals(2, asks.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 0, newPrice(1100, -2),
				newSize(17));
		book.make(entry);
		// System.out.println(book);

		assertEquals(3, asks.placeFromOffset(16 - 1));
		assertEquals(1, asks.placeFromOffset(0));
		assertEquals(1, asks.placeFromOffset(3));
		assertEquals(2, asks.placeFromOffset(4));
		assertEquals(2, asks.placeFromOffset(6));
		assertEquals(3, asks.placeFromOffset(10));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 1, newPrice(1050, -2),
				newSize(19));
		book.make(entry);
		// System.out.println(book);
		// System.out.println(asks.count());

		assertEquals(2, asks.placeFromOffset(4));
		assertEquals(3, asks.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 0, newPrice(1425, -2),
				newSize(21));
		book.make(entry);
		// System.out.println(book);

		assertEquals(4, asks.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 0, newPrice(1400, -2),
				newSize(23));
		book.make(entry);
		// System.out.println(book);

		assertEquals(4, asks.placeFromOffset(16 - 2));
		assertEquals(5, asks.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 1, newPrice(1275, -2),
				newSize(25));
		book.make(entry);
		// System.out.println(book);

		assertEquals(1, asks.placeFromOffset(0));
		assertEquals(1, asks.placeFromOffset(2));
		assertEquals(2, asks.placeFromOffset(5));
		assertEquals(3, asks.placeFromOffset(6));
		assertEquals(3, asks.placeFromOffset(7));

		// ###

		assertTrue(book.isEmpty(Book.Side.BID));

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 1, newPrice(3275, -2),
				newSize(11));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);
		assertEquals(1, bids.placeCount());
		assertEquals(1, bids.placeFromOffset(0));
		assertEquals(1, bids.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 9, newPrice(3150, -2),
				newSize(13));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, NORMAL);
		assertEquals(2, bids.placeCount());
		assertEquals(2, bids.placeFromOffset(16 - 16));
		assertEquals(2, bids.placeFromOffset(16 - 6));
		assertEquals(1, bids.placeFromOffset(16 - 5));
		assertEquals(1, bids.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, -1, newPrice(3350, -2),
				newSize(15));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);
		assertEquals(3, bids.placeCount());
		assertEquals(3, bids.placeFromOffset(16 - 16));
		assertEquals(2, bids.placeFromOffset(16 - 8));
		assertEquals(1, bids.placeFromOffset(16 - 3));
		assertEquals(1, bids.placeFromOffset(16 - 1));

		entry = new DefBookEntry(
				REMOVE, Book.Side.BID, Book.Type.DEFAULT, 1, NULL_PRICE, null);
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);
		assertEquals(bids.count(), bids.placeCount());
		assertEquals(2, bids.placeCount());
		assertEquals(2, bids.placeFromOffset(16 - 16));
		assertEquals(1, bids.placeFromOffset(16 - 4));
		assertEquals(0, bids.placeFromOffset(16 - 1));

		entry = book.topFor(Book.Side.BID);
		assertEquals(entry.side(), Book.Side.BID);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(3275, -2));
		assertEquals(entry.sizeValue(), newSize(11));

	}

	@Test
	public void testComboMerge() {

		final SizeValue size = newSize(5);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.COMBINED, size, step);
		final UniBookRing asks = book.asks;
		final UniBookRing bids = book.bids;
		UniBookResult result;
		MarketDoBookEntry entry;

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, -1, newPrice(3275, -2),
				newSize(11));
		result = book.make(entry);
		// System.out.println(book);
		entry = book.topFor(Book.Side.BID);
		assertEquals(result, TOP);
		assertEquals(entry.side(), Book.Side.BID);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(3275, -2));
		assertEquals(entry.sizeValue(), newSize(11));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, -1, newPrice(3300, -2),
				newSize(13));
		result = book.make(entry);
		// System.out.println(book);
		entry = book.topFor(Book.Side.ASK);
		assertEquals(result, TOP);
		assertEquals(entry.side(), Book.Side.ASK);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(3300, -2));
		assertEquals(entry.sizeValue(), newSize(13));

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.IMPLIED, -1, newPrice(3275, -2),
				newSize(15));
		result = book.make(entry);
		// System.out.println(book);
		entry = book.topFor(Book.Side.BID);
		assertEquals(result, NORMAL);
		assertEquals(entry.side(), Book.Side.BID);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(3275, -2));
		assertEquals(entry.sizeValue(), newSize(11 + 15));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.IMPLIED, -1, newPrice(3300, -2),
				newSize(17));
		result = book.make(entry);
		// System.out.println(book);
		entry = book.topFor(Book.Side.ASK);
		assertEquals(result, NORMAL);
		assertEquals(entry.side(), Book.Side.ASK);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 1);
		assertEquals(entry.priceValue(), newPrice(3300, -2));
		assertEquals(entry.sizeValue(), newSize(13 + 17));

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.IMPLIED, -1, newPrice(3200, -2),
				newSize(19));
		result = book.make(entry);
		// System.out.println(book);
		entry = bids.get(newPrice(3200, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry.side(), Book.Side.BID);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 2);
		assertEquals(entry.priceValue(), newPrice(3200, -2));
		assertEquals(entry.sizeValue(), newSize(19));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.IMPLIED, -1, newPrice(3400, -2),
				newSize(21));
		result = book.make(entry);
		// System.out.println(book);
		entry = asks.get(newPrice(3400, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry.side(), Book.Side.ASK);
		assertEquals(entry.type(), Book.Type.COMBINED);
		assertEquals(entry.place(), 2);
		assertEquals(entry.priceValue(), newPrice(3400, -2));
		assertEquals(entry.sizeValue(), newSize(21));

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.IMPLIED, 1, newPrice(3250, -2),
				newSize(23));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.IMPLIED, 1, newPrice(3325, -2),
				newSize(25));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 0, newPrice(3200, -2),
				newSize(27));
		result = book.make(entry);
		// System.out.println(book);
		entry = bids.get(newPrice(3200, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry.sizeValue(), newSize(19 + 27));

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.DEFAULT, 0, newPrice(3325, -2),
				newSize(29));
		result = book.make(entry);
		// System.out.println(book);
		entry = asks.get(newPrice(3325, -2));
		assertEquals(result, NORMAL);
		assertEquals(entry.sizeValue(), newSize(25 + 29));

		//

	}

	@Test
	public void testEntries() {

		final SizeValue size = newSize(5);
		final PriceValue step = newPrice(25, -2);
		final UniBook book = new UniBook(Instrument.NULL,
				Book.Type.COMBINED, size, step);
		UniBookResult result;
		DefBookEntry entry;

		DefBookEntry[] entriesBid;
		DefBookEntry[] entriesAsk;

		entriesBid = book.entriesFor(Book.Side.BID);
		entriesAsk = book.entriesFor(Book.Side.ASK);

		assertEquals(0, entriesBid.length);
		assertEquals(0, entriesAsk.length);

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 0, newPrice(3200, -2),
				newSize(27));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);

		entry = new DefBookEntry(
				MODIFY, Book.Side.BID, Book.Type.DEFAULT, 0, newPrice(3125, -2),
				newSize(29));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, NORMAL);

		entriesBid = book.entriesFor(Book.Side.BID);
		assertEquals(2, entriesBid.length);

		//

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.IMPLIED, 0, newPrice(3225, -2),
				newSize(31));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, TOP);

		entry = new DefBookEntry(
				MODIFY, Book.Side.ASK, Book.Type.IMPLIED, 0, newPrice(3325, -2),
				newSize(33));
		result = book.make(entry);
		// System.out.println(book);
		assertEquals(result, NORMAL);

		entriesAsk = book.entriesFor(Book.Side.ASK);
		assertEquals(2, entriesAsk.length);

		//

		entry = new DefBookEntry(
				NOOP, Book.Side.BID, Book.Type.COMBINED, 2, newPrice(3125, -2),
				newSize(29));
		assertEquals(entry, entriesBid[0]);

		entry = new DefBookEntry(
				NOOP, Book.Side.BID, Book.Type.COMBINED, 1, newPrice(3200, -2),
				newSize(27));
		assertEquals(entry, entriesBid[1]);

		//

		entry = new DefBookEntry(
				NOOP, Book.Side.ASK, Book.Type.COMBINED, 1, newPrice(3225, -2),
				newSize(31));
		assertEquals(entry, entriesAsk[0]);

		entry = new DefBookEntry(
				NOOP, Book.Side.ASK, Book.Type.COMBINED, 2, newPrice(3325, -2),
				newSize(33));
		assertEquals(entry, entriesAsk[1]);

		//

	}

}
