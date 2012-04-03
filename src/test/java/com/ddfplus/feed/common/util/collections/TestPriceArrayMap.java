package com.ddfplus.feed.common.util.collections;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.values.api.PriceValue;

public class TestPriceArrayMap {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPut() {

		final PriceValue pricePoint = newPrice(1, -2);

		final PriceArrayMap<Integer> map = new PriceArrayMap<Integer>(
				pricePoint);

		assertEquals(map.size(), 0);
		assertEquals(map.keyStep(), pricePoint);
		assertTrue(map.keyHead().isNull());
		assertTrue(map.keyTail().isNull());

		//

		PriceValue p1 = newPrice(123, -2); // 1.23

		map.put(p1, 1);

		assertEquals(map.size(), 1);
		assertEquals(map.get(p1), (Integer) 1);

		//

		PriceValue p2 = newPrice(128, -2); // 1.28

		map.put(p2, 2);

		assertEquals(map.size(), 128 - 123 + 1);
		assertEquals(map.get(p2), (Integer) 2);

		//

		PriceValue p3 = newPrice(12, -1); // 1.20

		map.put(p3, 3);

		assertEquals(map.size(), 128 - 120 + 1);
		assertEquals(map.get(p3), (Integer) 3);

		//

		PriceValue p4 = newPrice(1211, -3); // 1.21

		assertNull(map.get(p4));

		//

		PriceValue p5 = newPrice(123123, -5); // 1.23

		assertEquals(map.get(p5), (Integer) 1);
		assertEquals(map.get(p5), map.get(p1));

		//

		assertEquals(map.get(0), map.get(p3)); // 1.20
		assertEquals(map.get(1), null); // 1.21
		assertEquals(map.get(2), null); // 1.22
		assertEquals(map.get(3), map.get(p1)); // 1.23
		assertEquals(map.get(4), null); // 1.24
		assertEquals(map.get(5), null);// 1.25
		assertEquals(map.get(6), null);// 1.26
		assertEquals(map.get(7), null);// 1.27
		assertEquals(map.get(8), map.get(p2));// 1.28

		//

		assertEquals(map.put(p1, -1), new Integer(1));
		assertEquals(map.put(p1, 1), new Integer(-1));

		//

		assertEquals(map.getIndex(p1), 3);
		assertEquals(map.getIndex(p2), 8);
		assertEquals(map.getIndex(p3), 0);
		assertEquals(map.getIndex(p4), 1);
		assertEquals(map.getIndex(p5), 3);

		assertEquals(map.getIndex(newPrice(11900, -4)),
				ScadecArrayMap.ERROR_INDEX);
		assertEquals(map.getIndex(newPrice(12900, -4)),
				ScadecArrayMap.ERROR_INDEX);

		//

	}

}
