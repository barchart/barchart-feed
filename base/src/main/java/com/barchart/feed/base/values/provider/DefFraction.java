package com.barchart.feed.base.values.provider;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

import com.barchart.feed.base.values.api.Fraction;
import com.barchart.feed.base.values.api.PriceValue;

public class DefFraction extends BaseDecimal implements Fraction {

	private final int base;
	private final int exponent;
	private final long numerator;
	private final long denominator;
	
	private final int decimalExponent;
	private final long decimalDenominator;
	
	private final int places;
	
	DefFraction(final int base, final int exponent) {
		
		this.base = base;
		this.exponent = exponent;
		
		numerator = 1l;
		denominator = (long) pow(base, -exponent);
		
		decimalExponent = exponent;
		decimalDenominator = (long) pow(10, -decimalExponent);
		
		places = 1 + (int)Math.log10(denominator);
		
	}
	
	@Override
	public long mantissa() {
		return 0;
	}

	@Override
	public long base() {
		return base;
	}

	@Override
	public int exponent() {
		return exponent;
	}

	@Override
	public long numerator() {
		return numerator;
	}

	@Override
	public long denominator() {
		return denominator;
	}

	@Override
	public int decimalExponent() {
		return exponent;
	}

	@Override
	public long decimalDenominator() {
		return decimalDenominator;
	}

	@Override
	public int places() {
		return places;
	}
	
	@Override
	public boolean isSmallerThan(final Fraction that) {
		if (that == null) {
			return false;
		}
		return decimalExponent < that.decimalExponent();
	}

	@Override
	public long priceFraction(final PriceValue price) {
		if(price == null || price.isNull()) {
			return 0;
		}
		return priceFraction(price.mantissa(), price.exponent());
	}

	@Override
	public long priceFraction(long mantissa, int exponent) {
		
		while (exponent > decimalExponent) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < decimalExponent) {
			mantissa /= 10;
			exponent++;
		}

		mantissa = abs(mantissa);
		mantissa %= decimalDenominator;
		mantissa *= denominator;
		mantissa /= decimalDenominator;
		
		return mantissa;
	}
	
	@Override 
	public long priceWhole(PriceValue price) {
		
		if(price == null) {
			price = ValueConst.NULL_PRICE;
		}
		return priceWhole(price.mantissa(), price.exponent());
	}

	@Override
	public long priceWhole(long mantissa, int exponent) {
		
		while (exponent > decimalExponent) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < decimalExponent) {
			mantissa /= 10;
			exponent++;
		}

		return mantissa / decimalDenominator;
	}

}
