/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.enums;

import static com.barchart.feed.inst.enums.CodeCFI.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestCodeCFI {

	@Test
	public void testFromCode() {

		assertEquals(EQUITY, fromCode("E"));
		assertEquals(EQUITY, fromCode("EX"));
		assertEquals(EQUITY, fromCode("EXXXXX"));

		//

		assertEquals(FUTURE, fromCode("F"));
		assertEquals(FUTURE_SPREAD, fromCode("FM"));

		//

		assertEquals(OPTION, fromCode("O"));
		assertEquals(OPTION_SPREAD, fromCode("OM"));
		assertEquals(OPTION_CALL, fromCode("OC"));
		assertEquals(OPTION_PUT, fromCode("OP"));

	}

	@Test
	public void testGetDescription() {

		assertEquals("FUTURE SPREAD", FUTURE_SPREAD.getDescription());

		assertEquals("OPTION CALL AMERICAN",
				OPTION_CALL_AMERICAN.getDescription());

	}

}
