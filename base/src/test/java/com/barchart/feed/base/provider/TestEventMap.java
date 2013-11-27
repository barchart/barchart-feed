/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.thread.Runner;

public class TestEventMap {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMap() {

		final EventMap<Integer> enumMap = new EventMap<Integer>();
		assertTrue(enumMap.isEmpty());

		enumMap.put(MarketEvent.MARKET_STATUS_CLOSED, 123);
		assertFalse(enumMap.isEmpty());

		final int value = enumMap.get(MarketEvent.MARKET_STATUS_CLOSED);
		assertEquals(value, 123);

		final AtomicInteger count = new AtomicInteger(0);

		final Runner<Void, Entry<MarketEvent, Integer>> mapTask = //
		new Runner<Void, Entry<MarketEvent, Integer>>() {

			@Override
			public Void run(final Entry<MarketEvent, Integer> entry) {
				assertEquals(entry.getKey(), MarketEvent.MARKET_STATUS_CLOSED);
				assertEquals(entry.getValue(), new Integer(123));
				count.incrementAndGet();
				return null;
			}

		};

		enumMap.runLoop(mapTask, null);

		assertEquals(count.get(), 1);

		final int removed = enumMap.remove(MarketEvent.MARKET_STATUS_CLOSED);
		assertEquals(removed, 123);
		assertTrue(enumMap.isEmpty());

	}

}
