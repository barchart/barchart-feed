package com.ddfplus.util.lang;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.ascii.ASCII;

public class TestASCII {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	final static byte[] normal = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
			.getBytes(ASCII.ASCII_CHARSET);

	final static byte[] upper = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`ABCDEFGHIJKLMNOPQRSTUVWXYZ{|}~"
			.getBytes(ASCII.ASCII_CHARSET);

	final static byte[] lower = " !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
			.getBytes(ASCII.ASCII_CHARSET);

	@Test
	public void testToLowerCase() {

		final byte[] array = normal.clone();

		assertTrue(Arrays.equals(array, normal));

		ASCII.toLowerCase(array);

		assertTrue(Arrays.equals(array, lower));

	}

	@Test
	public void testToUpperCase() {

		final int start = 32;
		final int end = 126;
		final int size = end - start + 1;
		final byte[] array = new byte[size];

		for (int k = 0; k < size; k++) {
			array[k] = (byte) (k + start);
		}

		// System.out.println("" + new String(array, ASCII.CHARSET));

		assertTrue(Arrays.equals(array, normal));

		ASCII.toUpperCase(array);

		// System.out.println("" + new String(array, ASCII.CHARSET));

		assertTrue(Arrays.equals(array, upper));

	}

}
