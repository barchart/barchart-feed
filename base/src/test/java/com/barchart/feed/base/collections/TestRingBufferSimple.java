/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.common.bench.JavaSize;

public class TestRingBufferSimple {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRingArray1() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);

		int ringMemSize;
		ringMemSize = JavaSize.of(ring);
		// System.err.println("ring mem size=" + ringMemSize);
		assertEquals(ringMemSize, 72);

		//

		int basis;

		for (int k = 0; k < 10; k++) {
			ring.set(k, new Integer(k));
		}

		basis = ring.head();
		// System.err.println("ring basis=" + basis);
		assertEquals(basis, 0);

		//

		for (int k = 0; k < 10; k++) {
			assertEquals(ring.get(k), (Integer) k);
		}

		//

		assertEquals(ring.head(), 0);
		assertEquals(ring.tail(), 9);
		assertEquals(ring.get(4), (Integer) 4);

		for (int k = 5; k < 10; k++) {
			// System.err.println("ring k=" + k);
			assertEquals(ring.get(k), (Integer) (k));
		}

		//

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetException1() {
		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		ring.setHead(100, 100);
		ring.get(99);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetException2() {
		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		ring.setHead(100, 100);
		ring.get(110);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSetException1() {
		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		ring.setHead(100, 100);
		ring.set(99, 99);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSetException2() {
		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		ring.setHead(100, 100);
		ring.set(110, 110);
	}

	@Test
	public void testHead() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		assertEquals(ring.length(), 10);

		ring.setHead(100, 100);
		assertEquals(ring.count(), 1);
		assertEquals(ring.get(100), (Integer) 100);

		for (int k = 101; k < 110; k++) {
			assertEquals(ring.get(k), null);
		}

		for (int k = 100; k < 110; k++) {
			ring.set(k, k);
		}

		assertEquals(ring.count(), 10);

		for (int k = 100; k < 110; k++) {
			assertEquals(ring.get(k), (Integer) k);
		}

		ring.setHead(105, 205);

		assertEquals(ring.count(), 5);
		assertEquals(ring.head(), 105);
		assertEquals(ring.tail(), 114);

		assertEquals(ring.get(105), (Integer) 205);

		for (int k = 106; k < 110; k++) {
			assertEquals(ring.get(k), (Integer) k);
		}

		for (int k = 110; k < 115; k++) {
			assertEquals(ring.get(k), (Integer) null);
		}

		ring.set(114, 114);

		assertEquals(ring.count(), 6);
		assertEquals(ring.get(114), (Integer) 114);

	}

	@Test
	public void testyTail() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		assertEquals(ring.length(), 10);

		ring.setTail(109, 109);
		assertEquals(ring.count(), 1);
		assertEquals(ring.get(109), (Integer) 109);

		for (int k = 100; k < 109; k++) {
			assertEquals(ring.get(k), null);
		}

		for (int k = 100; k < 110; k++) {
			ring.set(k, k);
		}

		assertEquals(ring.count(), 10);

		for (int k = 100; k < 110; k++) {
			assertEquals(ring.get(k), (Integer) k);
		}

		ring.setTail(104, 204);
		assertEquals(ring.count(), 5);
		assertEquals(ring.head(), 95);
		assertEquals(ring.tail(), 104);
		assertEquals(ring.get(104), (Integer) 204);

		for (int k = 100; k < 104; k++) {
			assertEquals(ring.get(k), (Integer) k);
		}

		for (int k = 95; k < 100; k++) {
			assertEquals(ring.get(k), (Integer) null);
		}

		ring.set(95, 95);
		assertEquals(ring.get(95), (Integer) 95);
		assertEquals(ring.count(), 6);

	}

	@Test
	public void testAsArray() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		assertEquals(ring.length(), 10);

		ring.setHead(100, 100);

		for (int k = 100; k < 110; k++) {
			ring.set(k, k);
		}

		ring.set(102, null);
		ring.set(107, null);
		assertEquals(ring.count(), 8);

		final Integer[] clone1 = ring.asArray(Integer.class);
		final Integer[] clone2 = ring.asArray(Integer.class);

		assertFalse(clone1 == clone2);
		assertTrue(Arrays.equals(clone1, clone2));

		for (int k = 0; k < 10; k++) {
			assertEquals(ring.get(100 + k), clone1[k]);
			assertEquals(ring.get(100 + k), clone2[k]);
		}

	}

	@Test
	public void testAsList() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		assertEquals(ring.length(), 10);

		ring.setHead(100, 100);

		for (int k = 100; k < 110; k++) {
			ring.set(k, k);
		}

		ring.set(102, null);
		ring.set(107, null);
		assertEquals(ring.count(), 8);

		final List<Integer> list = ring.asList();

		assertEquals(list.size(), ring.count());

		int index = 0;
		for (int k = 100; k < 110; k++) {
			final Integer ringItem = ring.get(k);
			if (ringItem == null) {
				continue;
			}
			final Integer listItem = list.get(index++);
			assertEquals(ringItem, listItem);
		}

	}

	@Test
	public void testAsMap() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(10);
		assertEquals(ring.length(), 10);

		ring.setHead(100, 100);

		for (int k = 101; k < 110; k++) {
			ring.set(k, k);
		}

		ring.set(102, null);
		ring.set(107, null);
		assertEquals(ring.count(), 8);

		final Map<Integer, Integer> map = ring.asMap();

		assertEquals(map.size(), ring.count());

		// SortedMap<Integer, Integer> map1 = new TreeMap<Integer,
		// Integer>(map);

		for (int k = 100; k < 110; k++) {
			final Integer ringItem = ring.get(k);
			final Integer mapItem = map.get(k);
			assertEquals(ringItem, mapItem);
		}

	}

	@Test
	public void testHead1() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(1);
		assertEquals(ring.length(), 1);

		ring.setHead(100, 100);
		assertEquals(ring.head(), 100);
		assertEquals(ring.get(100), (Integer) 100);

		ring.setHead(123, 100);
		assertEquals(ring.head(), 123);
		assertEquals(ring.get(123), (Integer) 100);

	}

	@Test
	public void testTail1() {

		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(1);
		assertEquals(ring.length(), 1);

		ring.setTail(100, 100);
		assertEquals(ring.tail(), 100);
		assertEquals(ring.get(100), (Integer) 100);

		ring.setTail(123, 100);
		assertEquals(ring.tail(), 123);
		assertEquals(ring.get(123), (Integer) 100);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testRing1() {
		final RingBuffer<Integer> ring = new RingBufferSimple<Integer>(0);
	}

}
