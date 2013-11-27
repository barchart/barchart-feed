package com.barchart.feed.base.provider;

import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.util.value.FactoryImpl;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public final class ValueConverter {

	private static final Factory factory = new FactoryImpl();
	
	public static Price price(final PriceValue value) {
		return factory.newPrice(value.mantissa(), value.exponent());
	}
	
	public static Size size(final SizeValue value) {
		return factory.newSize(value.asLong(), 0);
	}
	
	public static Time time(final TimeValue value) {
		return factory.newTime(value.asMillisUTC(), "");
	}
	
	public static Fraction fraction(final com.barchart.feed.base.values.api.Fraction frac) {
		return factory.newFraction((int)frac.base(), (int)frac.exponent());
	}
	
}
