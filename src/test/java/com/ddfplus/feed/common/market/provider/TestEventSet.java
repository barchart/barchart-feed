package com.ddfplus.feed.common.market.provider;

import static com.ddfplus.feed.api.market.enums.MarketEvent.*;
import static java.lang.System.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.common.util.concurrent.Runner;

public class TestEventSet {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIterator1() {
		EventSet eventSet = new EventSet();
		int count = 0;
		for (MarketEvent event : eventSet) {
			count++;
			out.println(event);
		}
		assertEquals(count, 0);
	}

	@Test
	public void testIterator2() {
		EventSet eventSet = new EventSet();
		eventSet.add(MARKET_CLOSED);
		int count = 0;
		for (MarketEvent event : eventSet) {
			count++;
			out.println(event);
		}
		assertEquals(count, 1);
	}

	@Test
	public void testIterator3() {
		EventSet eventSet = new EventSet();
		eventSet.add(MARKET_CLOSED);
		eventSet.clear();
		int count = 0;
		for (MarketEvent event : eventSet) {
			count++;
			out.println(event);
		}
		assertEquals(count, 0);
	}

	@Test
	public void testIterator4() {

		EventSet eventSet = new EventSet();

		boolean isAdded;

		isAdded = eventSet.add(NEW_BOOK_SNAPSHOT);
		assertTrue(isAdded);

		isAdded = eventSet.add(NEW_BOOK_SNAPSHOT);
		assertFalse(isAdded);

		eventSet.add(MARKET_UPDATED);
		eventSet.add(NEW_CLOSE);

		eventSet.add(NEW_INTEREST);
		eventSet.add(NEW_INTEREST);

		Map<MarketEvent, Object> map = new HashMap<MarketEvent, Object>();

		int count = 0;

		for (MarketEvent event : eventSet) {
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

		EventSet eventSet = new EventSet();

		Iterator<MarketEvent> iterator = eventSet.iterator();

		assertFalse(iterator.hasNext());

		iterator.next();

	}

	@Test
	public void testRunBatch() {

		EventSet eventSet = new EventSet();

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
			public Void run(MarketEvent param) {
				count.incrementAndGet();
				return null;
			}
		};

		eventSet.runLoop(task, null);

		assertEquals(count.get(), 4);

	}

}
