/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.values.api.DecimalValue;
import com.barchart.feed.base.values.provider.ValueBuilder;

class DecimalValueDes extends ScaledValueDes<DecimalValue, DecimalValue> {

	static Logger log = LoggerFactory.getLogger(DecimalValueDes.class);

	protected DecimalValueDes(Class<DecimalValue> klaz) {
		super(klaz);
	}

	@Override
	protected DecimalValue newValue(long mantissa, int exponent) {
		return ValueBuilder.newDecimal(mantissa, exponent);
	}

}
