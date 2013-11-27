package com.barchart.feed.base.values.api;

public interface Fraction extends DecimalValue {
	
	long base();
	
	int exponent();
	
	long numerator();
	
	long denominator();
	
	int decimalExponent();
	
	long decimalDenominator();
	
	int places();

	boolean isSmallerThan(Fraction that);
	
	long priceFraction(PriceValue price);
	
	long priceFraction(long mantissa, int exponent);
	
	long priceWhole(PriceValue price);
	
	long priceWhole(long mantissa, int exponent);
	
}
