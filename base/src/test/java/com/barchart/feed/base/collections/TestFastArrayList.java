/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
	public void testToArray1() {

		final Integer[] array1 = { 1, 2, 3 };

		final FastArrayList<Integer> list = new FastArrayList<Integer>(array1);

		final Object[] array2 = list.toArray();

		assertNotNull(array2);
		assertEquals(array2.length, 3);

		assertArrayEquals(array1, array2);
		assertTrue(array1 == array2);

		list.remove((Integer) 3);
		list.add(3);

		final Object[] array3 = list.toArray();

		assertArrayEquals(array1, array3);
		assertFalse(array1 == array3);

	}

	@Test
	public void testToArray2() {

		final Integer[] array1 = { 1, 2, 3 };

		final FastArrayList<Integer> list = new FastArrayList<Integer>(array1);

		final Integer[] array2 = list.toArray(new Integer[0]);

		assertNotNull(array2);

		assertEquals(array2.length, 3);

		assertArrayEquals(array1, array2);

		assertFalse(array1 == array2);

	}

	@Test
	public void testIterator() {

		boolean isAdded, isRemoved;

		final FastArrayList<Integer> array = new FastArrayList<Integer>();

		assertTrue(array.isEmpty());

		final Integer one = 123456;

		final Integer two = 654321;

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
		for (final Integer item : array) {
			count1++;
		}
		assertEquals(count1, 2);

		int count2 = 0;
		for (final Integer item : array) {
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
