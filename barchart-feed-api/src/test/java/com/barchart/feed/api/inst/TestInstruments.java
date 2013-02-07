/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.inst;

import static com.barchart.feed.api.fields.InstrumentField.*;
import static com.barchart.feed.api.inst.TestValues.*;

import com.barchart.missive.core.TagMapSafe;
import com.barchart.missive.hash.HashTagMapSafe;

public final class TestInstruments {

	public static final TagMapSafe INST_MAP_1 = new HashTagMapSafe(FIELDS);
	
	static {
		INST_MAP_1.set(MARKET_GUID, GUID_V_1);
		INST_MAP_1.set(SECURITY_TYPE, SEC_TYPE_V_1);
		INST_MAP_1.set(BOOK_LIQUIDITY, BOOK_LIQ_TYPE_V_1);
		INST_MAP_1.set(BOOK_STRUCTURE, BOOK_STRCT_V_1);
		INST_MAP_1.set(BOOK_DEPTH, BOOK_DEPTH_V_1);
		INST_MAP_1.set(VENDOR, VENDOR_V_1);
		INST_MAP_1.set(SYMBOL, SYMBOL_V_1);
		INST_MAP_1.set(DESCRIPTION, DESCRIPTION_V_1);
		INST_MAP_1.set(CFI_CODE, CFI_CODE_V_1);
		INST_MAP_1.set(CURRENCY_CODE, CURRENCY_V_1);
		INST_MAP_1.set(EXCHANGE_CODE, EXCHANGE_CODE_V_1);
		INST_MAP_1.set(PRICE_STEP, PRICE_STEP_V_1);
		INST_MAP_1.set(POINT_VALUE, POINT_VALUE_V_1);
		INST_MAP_1.set(DISPLAY_FRACTION, DISP_FRAC_V_1);
		INST_MAP_1.set(LIFETIME, LIFETIME_V_1);
		INST_MAP_1.set(MARKET_HOURS, MKT_HOURS_V_1);
		INST_MAP_1.set(TIME_ZONE_OFFSET, TIME_ZONE_OFFSET_V_1);
		INST_MAP_1.set(TIME_ZONE_NAME, TIME_ZONE_NAME_V_1);
	}
	
	public static final TagMapSafe INST_MAP_2 = new HashTagMapSafe(FIELDS);
	public static final TagMapSafe INST_MAP_3 = new HashTagMapSafe(FIELDS);
	
	public static final TagMapSafe[] INSTS = new TagMapSafe[] {
		INST_MAP_1 //, INST_MAP_2, INST_MAP_3
	};
	
	
}
