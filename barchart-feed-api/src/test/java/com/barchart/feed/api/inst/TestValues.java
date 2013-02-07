/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.inst;

import static com.barchart.util.values.provider.ValueBuilder.*;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeInterval;

public final class TestValues {

	public static final TextValue GUID_V_1 = newText("123456");
	public static final SecurityType SEC_TYPE_V_1 = SecurityType.FUTURE;
	public static final BookLiquidityType BOOK_LIQ_TYPE_V_1 = BookLiquidityType.DEFAULT;
	public static final BookStructureType BOOK_STRCT_V_1 = BookStructureType.PRICE_LEVEL;
	public static final SizeValue BOOK_DEPTH_V_1 = newSize(10);
	public static final TextValue VENDOR_V_1 = newText("vendor1");
	public static final TextValue SYMBOL_V_1 = newText("symbol1");
	public static final TextValue DESCRIPTION_V_1 = newText("description1");
	public static final TextValue CFI_CODE_V_1 = newText("FXXXXX");
	public static final MarketCurrency CURRENCY_V_1 = MarketCurrency.USD;
	public static final TextValue EXCHANGE_CODE_V_1 = newText("exchangeID1");
	public static final PriceValue PRICE_STEP_V_1 = newPrice(25, -1);
	public static final PriceValue POINT_VALUE_V_1 = newPrice(500, 1);
	public static final Fraction DISP_FRAC_V_1 = newFraction(10, -2);
	public static final TimeInterval LIFETIME_V_1 = newTimeInterval(100, 100000);
	public static final TimeInterval[] MKT_HOURS_V_1 = new TimeInterval[] {
		newTimeInterval(1000,5000), newTimeInterval(10000,20000)
	};
	public static final SizeValue TIME_ZONE_OFFSET_V_1 = newSize(-6 * 60 * 60 * 1000);
	public static final TextValue TIME_ZONE_NAME_V_1 = newText("CST");
	
}
