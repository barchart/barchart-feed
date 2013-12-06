/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.feed.base.provider.DefBookEntry;
import com.barchart.feed.base.values.provider.ValueConst;
import com.barchart.util.common.bench.JavaSize;

public class TestDefBookEntry {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJavaSize1() {

		final DefBookEntry entry = new DefBookEntry(null, null, null, 
				0, null, null);

		final int entrySize = JavaSize.of(entry);

		// System.out.println("entrySize=" + entrySize);

		assertEquals(entrySize, 24);

	}

	@Test
	public void testNullDefaults() {

		final DefBookEntry entry = new DefBookEntry(null, null, null, 
				0, null, null);

		assertEquals(entry.act(), MarketBookAction.NOOP);
		assertEquals(entry.side(), Book.Side.NULL);
		assertEquals(entry.type(), Book.Type.NONE);

		assertEquals(entry.place(), 0);
		assertEquals(entry.priceValue(), ValueConst.NULL_PRICE);
		assertEquals(entry.sizeValue(), ValueConst.NULL_SIZE);

	}

	@Test(expected = ArithmeticException.class)
	public void testPlaceRange1() {
		new DefBookEntry(null, null, null, +200, null, null);
	}

	@Test(expected = ArithmeticException.class)
	public void testPlaceRange2() {
		new DefBookEntry(null, null, null, -200, null, null);
	}

}
