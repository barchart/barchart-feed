package com.barchart.feed.api.model.meta.instrument;

import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Price;

/**
 * Price display formatter and parser.
 */
public interface PriceFormat extends Existential {

	enum SubFormat {

		/**
		 * Display as a single number, with no decimal or fractional separators,
		 * and formatted according to the fraction denominator.
		 *
		 * For example, 12.5 with a denominator of 8 would be displayed as "124"
		 */
		FLAT,

		/**
		 * Display as a fraction, with components separated by a dash, and the
		 * right hand side formatted according to the fraction denonmator.
		 *
		 * For example, 12.5 with a denominator of 8 would be displayed as
		 * "12-4"
		 */
		FRACTIONAL,

		/**
		 * Display as a decimal, with components separated by a period.
		 *
		 * For example, 12.5 would be displayed as "12.5"
		 */
		DECIMAL

	};

	/**
	 * Denominator of main fraction.
	 */
	int denominator();

	/**
	 * Numerator precision for main fraction.
	 */
	int precision();

	/**
	 * True if price should be displayed as a fraction rather than a decimal.
	 */
	boolean fractional();

	/**
	 * Denominator of sub-fraction, if the numerator of the main fraction is a
	 * fraction itself. If the numerator is not a fraction, this will return 0.
	 */
	int subDenominator();

	/**
	 * Numerator precision of sub-fraction, if the numerator of the main
	 * fraction is a fraction itself. If the numerator is not a fraction, this
	 * will return 0.
	 */
	int subPrecision();

	/**
	 * Display format of sub-fraction, if the numerator of the main fraction is
	 * a fraction itself.
	 */
	SubFormat subFormat();

	/**
	 * Format the given price for display.
	 */
	String format(Price price);

	/**
	 * Parse a formatted price according to the formatting rules.
	 */
	Price parse(String formatted);

	static PriceFormat NULL = new PriceFormat() {

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public String format(final Price price) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price parse(final String formatted) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int denominator() {
			return 0;
		}

		@Override
		public int precision() {
			return 0;
		}

		@Override
		public boolean fractional() {
			return false;
		}

		@Override
		public int subDenominator() {
			return 0;
		}

		@Override
		public int subPrecision() {
			return 0;
		}

		@Override
		public SubFormat subFormat() {
			return SubFormat.FLAT;
		}

	};

}
