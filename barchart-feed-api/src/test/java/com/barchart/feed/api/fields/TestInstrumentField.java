package com.barchart.feed.api.fields;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.fields.InstrumentField;
import com.barchart.missive.core.Tag;

public class TestInstrumentField implements InstrumentField {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void tearDown() throws Exception {

		assertTrue(InstrumentField.FIELDS.length > 0);

		for (final Tag<?> tag : InstrumentField.FIELDS) {
			// log.info("tag : {}", tag);
			assertTrue(tag.name() != null);
		}

	}

}
