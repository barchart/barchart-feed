/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import static com.barchart.feed.base.values.provider.ValueBuilder.newPrice;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_PRICE;

import com.barchart.feed.base.values.api.DecimalValue;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.lang.ScaledDecimalValue;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
abstract class BasePrice extends ScaledDecimalValue<PriceValue, DecimalValue>
		implements PriceValue {

	@Override
	protected PriceValue result(final long mantissa, final int exponent) {
		return newPrice(mantissa, exponent);
	}

	@Override
	public final boolean isNull() {
		return this == NULL_PRICE;
	}

	@Override
	public final boolean equals(final Object thatValue) {
		if (thatValue instanceof PriceValue) {
			PriceValue that = (PriceValue) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public final double asDouble() {
		return mantissa() * Math.pow(10, exponent());
	}
	
}
