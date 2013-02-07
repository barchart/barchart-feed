/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.util.math.MathExtra;

public class TestMarketBookAction {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFrom() {

		MathExtra.castIntToByte(MarketBookAction.values().length);

		for (final MarketBookAction act : MarketBookAction.values()) {
			final byte ord = MathExtra.castIntToByte(act.ordinal());
			assertEquals(act, MarketBookAction.fromOrd(ord));
		}

	}

}
