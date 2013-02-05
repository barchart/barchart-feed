package com.barchart.feed.inst.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestInstrumentField implements InstrumentField {

	@Test
	public void tearDown() throws Exception {
		assertTrue(InstrumentField.FIELDS.length > 0);
	}

}
