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
import com.barchart.util.math.MathExtra;

public class TestMarketBookSide {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFrom() {

		MathExtra.castIntToByte(Book.Side.values().length);

		for (final Book.Side side : Book.Side.values()) {
			final byte ord = MathExtra.castIntToByte(side.ordinal());
			assertEquals(side, Book.Side.fromOrd(ord));
		}

	}

}
