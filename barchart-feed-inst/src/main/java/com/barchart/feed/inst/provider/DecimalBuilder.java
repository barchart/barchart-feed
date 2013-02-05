package com.barchart.feed.inst.provider;

import com.barchart.proto.buf.inst.Decimal;
import com.barchart.util.values.api.PriceValue;

public final class DecimalBuilder {
	
	private static final Decimal.Builder builder = Decimal.newBuilder();
	
	public static Decimal build(final long mantissa, final int exponent) {
		
		builder.clear();
		
		builder.setMantissa(mantissa);
		builder.setExponent(exponent);
		
		return builder.build();
		
	}
	
	public static Decimal build(final PriceValue price) {
		
		builder.clear();
		
		builder.setMantissa(price.mantissa());
		builder.setExponent(price.exponent());
		
		return builder.build();
	}

}
