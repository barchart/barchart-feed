package com.ddfplus.market.api.enums;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ddfplus.feed.api.market.enums.MarketEvent;

public class TestMarketEvent {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSize() {

		System.out.println("MarketEvent.enumSize()=" + MarketEvent.size());

		assertTrue(true);
	}

}
