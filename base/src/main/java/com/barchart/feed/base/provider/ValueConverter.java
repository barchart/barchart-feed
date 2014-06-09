package com.barchart.feed.base.provider;

import com.barchart.feed.base.values.api.BooleanValue;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Bool;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.ValueFactory;

public final class ValueConverter {

	private static final ValueFactory factory = ValueFactoryImpl.instance;
	
	public static Price price(final PriceValue value) {
		if(value.isNull()) {
			return Price.NULL;
		}
		return factory.newPrice(value.mantissa(), value.exponent());
	}
	
	public static Size size(final SizeValue value) {
		if(value.isNull()) {
			return Size.NULL;
		}
		return factory.newSize(value.asLong(), 0);
	}
	
	public static Time time(final TimeValue value) {
		if(value.isNull()) {
			return Time.NULL;
		}
		return factory.newTime(value.asMillisUTC());
	}
	
	public static Fraction fraction(final com.barchart.feed.base.values.api.Fraction frac) {
		if(frac.isNull()) {
			return Fraction.NULL;
		}
		return factory.newFraction((int)frac.base(), frac.exponent());
	}
	
	public static Bool bool(final BooleanValue value) {
		if(value.isNull()) {
			return Bool.NULL;
		}
		return factory.newBoolean(value.asBoolean());
	}

}
