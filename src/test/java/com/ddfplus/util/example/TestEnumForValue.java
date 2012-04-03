package com.ddfplus.util.example;

import static java.lang.System.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEnumForValue {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEnumForValue() {

		EnumForValue<?>[] values = EnumForValue.values();

		out.println("values=" + values);

		for (EnumForValue<?> enumX : values) {

			out.println("" + enumX);

		}

		assertEquals(EnumForValue.VALUE_ONE.name(), "VALUE_ONE");

		assertEquals(EnumForValue.VALUE_TWO.name(), "VALUE_TWO");

		assertTrue(EnumForValue.VALUE_ONE.compareTo(EnumForValue.VALUE_TWO) < 0);

	}

}
