/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.values.api.BooleanValue;
import com.barchart.feed.base.values.api.DecimalValue;
import com.barchart.feed.base.values.api.Fraction;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TextValue;
import com.barchart.feed.base.values.api.TimeInterval;
import com.barchart.feed.base.values.api.TimeValue;

public final class ValueConst {

	private static final Logger log = LoggerFactory.getLogger(ValueConst.class);

	private ValueConst() {
	}

	public static final DecimalValue NULL_DECIMAL = //
	new DefDecimal(0, 0);

	public static final BooleanValue TRUE_BOOLEAN = //
	new DefBoolean(true);

	public static final BooleanValue FALSE_BOOLEAN = //
	new DefBoolean(false);

	public static final BooleanValue NULL_BOOLEAN = //
	new NulBoolean();

	public static final TextValue NULL_TEXT = //
	new NulText();

	public static final PriceValue NULL_PRICE = //
	new NulPrice();
	public static final PriceValue ZERO_PRICE = //
	new NulPrice();

	public static final SizeValue NULL_SIZE = //
	new NulSize();
	public static final SizeValue ZERO_SIZE = //
	new NulSize();

	public static final TimeValue NULL_TIME = //
	new NulTime();
	public static final TimeValue ZERO_TIME = //
	new NulTime();
	
	public static final TimeInterval NULL_TIME_INTERVAL = //
	new NulTimeInterval();

	public static final Fraction NULL_FRACTION = //
	new NulFraction();
	
	public static final SizeValue[] NULL_SIZE_ARRAY = new SizeValue[0];

	static {
		// sizeReport(ValueConst.class);
	}

}
