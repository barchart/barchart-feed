/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFastEnumSet {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	enum E1 implements BitSetEnum<E1> {

		a, b, c;

		private final long mask = ONE << ordinal();

		@Override
		public long mask() {
			return mask;
		}

		private final static E1[] VALUES = values();

		public static E1[] valuesUnsafe() {
			return VALUES;
		}

	}

	@Test
	public void testBitSetFast0() {

		// empty
		final Set<E1> set = new FastEnumSet<E1>(E1.values());

		int count;

		count = 0;
		for (final E1 event : set) {
			count++;
			out.println(event);
		}
		assertEquals(count, 0);

		set.add(E1.a);
		set.add(E1.c);

		count = 0;
		for (final E1 event : set) {
			count++;
			out.println(event.mask());
		}
		assertEquals(count, 2);

	}

	@Test
	public void testBitSetFast1() {

		final FastEnumSet<E1> eventSet1 = new FastEnumSet<E1>(E1.valuesUnsafe());
		final FastEnumSet<E1> eventSet2 = new FastEnumSet<E1>(E1.valuesUnsafe());

		assertTrue(eventSet1.enumValues == eventSet2.enumValues);

	}
}
