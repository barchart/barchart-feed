package com.ddfplus.feed.common.util.collections;

import static java.lang.System.*;
import static org.junit.Assert.*;

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
		Set<E1> set = new FastEnumSet<E1>(E1.values());

		int count;

		count = 0;
		for (E1 event : set) {
			count++;
			out.println(event);
		}
		assertEquals(count, 0);

		set.add(E1.a);
		set.add(E1.c);

		count = 0;
		for (E1 event : set) {
			count++;
			out.println(event.mask());
		}
		assertEquals(count, 2);

	}

	@Test
	public void testBitSetFast1() {

		FastEnumSet<E1> eventSet1 = new FastEnumSet<E1>(E1.valuesUnsafe());
		FastEnumSet<E1> eventSet2 = new FastEnumSet<E1>(E1.valuesUnsafe());

		assertTrue(eventSet1.enumValues == eventSet2.enumValues);

	}
}
