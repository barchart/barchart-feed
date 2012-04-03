package com.ddfplus.feed.api.instrument.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestInstrumentField {

	@Test
	public void testSize() {

		for (InstrumentField<?> field : InstrumentField.values()) {

			System.out.println("field : " + field);

		}

		int size = InstrumentField.size();

		System.out.println("size : " + size);

		assertTrue(true);

	}

}
