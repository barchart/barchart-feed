package com.ddfplus.market.impl.bok;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.math.MathExtra;
import com.ddfplus.feed.api.market.enums.MarketBookSide;

public class TestBookSide {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOf() {

		for (final MarketBookSide side : MarketBookSide.values()) {
			final byte ord = MathExtra.castIntToByte(side.ordinal());
			assertEquals(side, MarketBookSide.fromOrd(ord));
		}

	}

}
