/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.provider.ValueBuilder;

public class TestPriceRingBuffer {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPriceRingArray() {

		final int size = 10;
		final PriceValue step = ValueBuilder.newPrice(1, -2);

		final PriceRingBuffer<Integer> ring = new PriceRingBuffer<Integer>(
				size, step);

		assertEquals(ring.length(), size);

		ring.setHead(ValueBuilder.newPrice(1, 0), 100);

		for (int k = 0; k < 10; k++) {
			ring.set(ValueBuilder.newPrice(100 + k, -2), k);
		}

		assertEquals(ring.count(), size);

		for (int k = 0; k < 10; k++) {
			final Integer item = ring.get(ValueBuilder.newPrice(100 + k, -2));
			assertEquals(item, (Integer) k);
		}

		//

		ring.setHead(ValueBuilder.newPrice(105, -2), 105);

		for (int k = 6; k < 10; k++) {
			final Integer item = ring.get(ValueBuilder.newPrice(100 + k, -2));
			assertEquals(item, (Integer) k);
		}

		for (int k = 10; k < 15; k++) {
			final Integer item = ring.get(ValueBuilder.newPrice(100 + k, -2));
			assertEquals(item, (Integer) null);
		}

	}

}
