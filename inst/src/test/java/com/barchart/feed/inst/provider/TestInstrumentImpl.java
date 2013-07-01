/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.barchart.feed.inst.InstrumentField;
import com.barchart.missive.api.Tag;

public class TestInstrumentImpl {
	
	@Test
	public void testID() {
		
		final InstrumentGUID INST_GUID_1A = new InstrumentGUID("1");
		final InstrumentGUID INST_GUID_1B = new InstrumentGUID("1");
		
		final InstrumentGUID INST_GUID_2 = new InstrumentGUID("2");
		
		final InstrumentGUID INST_GUID_3 = new InstrumentGUID("3");
		
		final Map<Tag, Object> tagmap1 = new HashMap<Tag, Object>();
		
		tagmap1.put(InstrumentField.GUID, INST_GUID_1A);
		
		
		
	}

}
