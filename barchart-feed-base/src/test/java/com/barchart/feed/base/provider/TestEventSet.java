/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.market.enums.MarketEvent.*;
import static java.lang.System.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.provider.EventSet;
import com.barchart.util.thread.Runner;

public class TestEventSet {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIterator1() {
		final EventSet eventSet = new EventSet();
		int count = 0;
		for (final MarketEvent event : eventSet) {
			count++;
			out.println(event);
		}
		assertEquals(count, 0);
	}

	@Test
	public void testIterator2() {
		final EventSet eventSet = new EventSet();
		eventSet.add(MARKET_STATUS_CLOSED);
		int count = 0;
		for (final MarketEvent event : eventSet) {
			count++;
			out.println(event);
		}
		assertEquals(count, 1);
	}

	@Test
	public void testIterator3() {
		final EventSet eventSet = new EventSet();
		eventSet.add(MARKET_STATUS_CLOSED);
		eventSet.clear();
		int count = 0;
		for (final MarketEvent event : eventSet) {
			count++;
			out.println(event);
		}
		assertEquals(count, 0);
	}

	@Test
	public void testIterator4() {

		final EventSet eventSet = new EventSet();

		boolean isAdded;

		isAdded = eventSet.add(NEW_BOOK_SNAPSHOT);
		assertTrue(isAdded);

		isAdded = eventSet.add(NEW_BOOK_SNAPSHOT);
		assertFalse(isAdded);

		eventSet.add(MARKET_UPDATED);
		eventSet.add(NEW_CLOSE);

		eventSet.add(NEW_INTEREST);
		eventSet.add(NEW_INTEREST);

		final Map<MarketEvent, Object> map = new HashMap<MarketEvent, Object>();

		int count = 0;

		for (final MarketEvent event : eventSet) {
			map.put(event, event);
			count++;
			out.println(event);
		}

		assertEquals(count, 4);

		assertTrue(map.containsKey(NEW_BOOK_SNAPSHOT));
		assertTrue(map.containsKey(MARKET_UPDATED));
		assertTrue(map.containsKey(NEW_CLOSE));
		assertTrue(map.containsKey(NEW_INTEREST));

		assertFalse(eventSet.isEmpty());
		eventSet.clear();
		assertTrue(eventSet.isEmpty());

	}

	@Test(expected = IllegalStateException.class)
	public void testIterator5() {

		final EventSet eventSet = new EventSet();

		final Iterator<MarketEvent> iterator = eventSet.iterator();

		assertFalse(iterator.hasNext());

		iterator.next();

	}

	@Test
	public void testRunBatch() {

		final EventSet eventSet = new EventSet();

		boolean isAdded;

		isAdded = eventSet.add(NEW_BOOK_SNAPSHOT);
		assertTrue(isAdded);

		isAdded = eventSet.add(NEW_BOOK_SNAPSHOT);
		assertFalse(isAdded);

		eventSet.add(MARKET_UPDATED);
		eventSet.add(NEW_CLOSE);
		eventSet.add(NEW_INTEREST);
		eventSet.add(MARKET_UPDATED);
		eventSet.add(NEW_INTEREST);
		eventSet.add(NEW_CLOSE);

		final AtomicInteger count = new AtomicInteger(0);

		final Runner<Void, MarketEvent> task = //
		new Runner<Void, MarketEvent>() {
			@Override
			public Void run(final MarketEvent param) {
				count.incrementAndGet();
				return null;
			}
		};

		eventSet.runLoop(task, null);

		assertEquals(count.get(), 4);

	}

}
