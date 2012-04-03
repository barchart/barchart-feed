package com.ddfplus.feed.common.util.collections;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFastArrayList {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIterator() {

		boolean isAdded, isRemoved;

		FastArrayList<Integer> array = new FastArrayList<Integer>();

		assertTrue(array.isEmpty());

		Integer one = 123456;

		Integer two = 654321;

		isAdded = array.add(one);
		assertTrue(isAdded);
		isAdded = array.add(two);
		assertTrue(isAdded);
		assertEquals(array.size(), 2);

		isAdded = array.add(one);
		assertFalse(isAdded);
		isAdded = array.add(two);
		assertFalse(isAdded);
		assertEquals(array.size(), 2);

		int count1 = 0;
		for (Integer item : array) {
			count1++;
		}
		assertEquals(count1, 2);

		int count2 = 0;
		for (Integer item : array) {
			count2++;
		}
		assertEquals(count2, 2);

		isRemoved = array.remove(two);
		assertTrue(isRemoved);
		assertEquals(array.size(), 1);

		isRemoved = array.remove(two);
		assertFalse(isRemoved);
		assertEquals(array.size(), 1);

		isRemoved = array.remove(one);
		assertTrue(isRemoved);
		assertEquals(array.size(), 0);

		isRemoved = array.remove(one);
		assertFalse(isRemoved);
		assertEquals(array.size(), 0);

	}

}
