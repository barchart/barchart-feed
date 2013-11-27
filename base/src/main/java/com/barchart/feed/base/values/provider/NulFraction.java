package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.Fraction;
import com.barchart.feed.base.values.api.PriceValue;

class NulFraction extends BaseDecimal implements Fraction {

	@Override
	public long mantissa() {
		return 0;
	}

	@Override
	public long base() {
		return 1;
	}

	@Override
	public int exponent() {
		return 0;
	}

	@Override
	public long numerator() {
		return 0;
	}

	@Override
	public long denominator() {
		return 1;
	}

	@Override
	public int decimalExponent() {
		return 0;
	}

	@Override
	public long decimalDenominator() {
		return 1;
	}

	@Override
	public int places() {
		return 0;
	}

	@Override
	public boolean isSmallerThan(Fraction that) {
		return false;
	}

	@Override
	public long priceFraction(PriceValue price) {
		return 0;
	}

	@Override
	public long priceFraction(long mantissa, int exponent) {
		return 0;
	}

	@Override
	public long priceWhole(long mantissa, int exponent) {
		return 0;
	}

	@Override
	public long priceWhole(PriceValue price) {
		return 0;
	}

}
