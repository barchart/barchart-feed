/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.enums;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.feed.base.instrument.enums.InstrumentField;

public class TestInstrumentField {

	@Test
	public void testSize() {

		for (final InstrumentField<?> field : InstrumentField.values()) {

			System.out.println("field : " + field);

		}

		final int size = InstrumentField.size();

		System.out.println("size : " + size);

		assertTrue(true);

	}

}
