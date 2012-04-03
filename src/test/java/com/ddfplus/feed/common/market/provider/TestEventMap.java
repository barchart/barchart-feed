package com.ddfplus.feed.common.market.provider;

import static org.junit.Assert.*;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.common.util.concurrent.Runner;

public class TestEventMap {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMap() {

		EventMap<Integer> enumMap = new EventMap<Integer>();
		assertTrue(enumMap.isEmpty());

		enumMap.put(MarketEvent.MARKET_CLOSED, 123);
		assertFalse(enumMap.isEmpty());

		int value = enumMap.get(MarketEvent.MARKET_CLOSED);
		assertEquals(value, 123);

		final AtomicInteger count = new AtomicInteger(0);

		final Runner<Void, Entry<MarketEvent, Integer>> mapTask = //
		new Runner<Void, Entry<MarketEvent, Integer>>() {

			@Override
			public Void run(Entry<MarketEvent, Integer> entry) {
				assertEquals(entry.getKey(), MarketEvent.MARKET_CLOSED);
				assertEquals(entry.getValue(), new Integer(123));
				count.incrementAndGet();
				return null;
			}

		};

		enumMap.runLoop(mapTask, null);

		assertEquals(count.get(), 1);

		int removed = enumMap.remove(MarketEvent.MARKET_CLOSED);
		assertEquals(removed, 123);
		assertTrue(enumMap.isEmpty());

	}

}
