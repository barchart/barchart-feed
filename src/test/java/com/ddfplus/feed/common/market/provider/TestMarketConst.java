package com.ddfplus.feed.common.market.provider;

import static com.barchart.util.values.provider.ValueConst.*;
import static com.ddfplus.feed.common.market.provider.MarketConst.*;
import static java.lang.System.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMarketConst {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPrice() {
		out.println(NULL_PRICE);
		out.println();
		assertTrue(true);
	}

	@Test
	public void testSize() {
		out.println(NULL_SIZE);
		out.println();
		assertTrue(true);
	}

	@Test
	public void testTime() {
		out.println(NULL_TIME);
		out.println();
		assertTrue(true);
	}

	@Test
	public void testBookEntry() {
		out.println(NULL_CUVOL_ENTRY);
		out.println();
		assertTrue(true);
	}

	@Test
	public void testBook() {
		out.println(NULL_BOOK);
		out.println();
		assertTrue(true);
	}

	@Test
	public void testBookTop() {
		out.println(NULL_BOOK_TOP);
		out.println();
		assertTrue(true);
	}

	@Test
	public void testMarket() {
		out.println(NULL_MARKET);
		// out.println(NULL_MARKET.get(MarketField.BAR_CURRENT));
		out.println();
		assertTrue(true);
	}

}
