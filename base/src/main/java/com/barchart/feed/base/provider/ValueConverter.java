package com.barchart.feed.base.provider;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueBuilder;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public final class ValueConverter {

	public static Price price(final PriceValue value) {
		return ValueBuilder.newPrice(value.mantissa(), value.exponent());
	}
	
	public static Size size(final SizeValue value) {
		return ValueBuilder.newSize(value.asLong());
	}
	
	public static Time time(final TimeValue value) {
		return ValueBuilder.newTime(value.asMillisUTC());
	}
	
}
