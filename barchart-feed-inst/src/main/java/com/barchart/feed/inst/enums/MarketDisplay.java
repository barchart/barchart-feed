package com.barchart.feed.inst.enums;

import static com.barchart.feed.inst.enums.MarketDisplay.Base.BINARY;
import static com.barchart.feed.inst.enums.MarketDisplay.Base.DECIMAL;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N01;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N02;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N03;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N04;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N05;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N06;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N07;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N08;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.N09;
import static com.barchart.feed.inst.enums.MarketDisplay.Exponent.Z00;
import static java.lang.Math.abs;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.barchart.api.fields.InstrumentField;
import com.barchart.feed.inst.api.Instrument;
import com.barchart.util.ascii.ASCII;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueConst;

public enum MarketDisplay {
	
	/* TODO MAKE INTO INTERFACE */
	DEFAULT, //

	;

	// ############################################

	private static TimeValue filter(final TimeValue time) {
		if (time == null) {
			return ValueConst.NULL_TIME;
		}
		return time;
	}

	private final static DateTimeFormatter YEAR_FULL = DateTimeFormat
			.forPattern("yyyy");

	private final static DateTimeFormatter YEAR_SHORT = DateTimeFormat
			.forPattern("yy");

	private final static DateTimeFormatter MONTH_FULL = DateTimeFormat
			.forPattern("MMMMMMMMMMMM");

	private final static DateTimeFormatter MONTH_SHORT = DateTimeFormat
			.forPattern("MMM");

	/** such as 2011 */
	public static String timeYearFull(/* local */TimeValue time) {
		time = filter(time);
		return YEAR_FULL.print(time.asMillisUTC());
	}

	/** such as 2011 = 11 */
	public static String timeYearShort(/* local */TimeValue time) {
		time = filter(time);
		return YEAR_SHORT.print(time.asMillisUTC());
	}

	/** such as December */
	public static String timeMonthFull(/* local */TimeValue time) {
		time = filter(time);
		return MONTH_FULL.print(time.asMillisUTC());
	}

	/** such as December = DEC */
	public static String timeMonthShort(/* local */TimeValue time) {
		time = filter(time);
		return MONTH_SHORT.print(time.asMillisUTC()).toUpperCase();
	}

	/** such as future January = "F" */
	public static String timeMonthCode(/* local */TimeValue time) {

		time = filter(time);

		final DateTime date = new DateTime(time.asMillisUTC());

		final int month = date.getMonthOfYear();

		switch (month) {
		case 1:
			return "F";
		case 2:
			return "G";
		case 3:
			return "H";
		case 4:
			return "J";
		case 5:
			return "K";
		case 6:
			return "M";
		case 7:
			return "N";
		case 8:
			return "Q";
		case 9:
			return "U";
		case 10:
			return "V";
		case 11:
			return "X";
		case 12:
			return "Z";
		default:
			return "?";
		}

	}

	public static long priceFraction(/* local */PriceValue price,
	/* local */Fraction frac) {

		if (price == null) {
			price = ValueConst.NULL_PRICE;
		}

		if (frac == null) {
			frac = Fraction.DEC_Z00;
		}

		return priceFraction(price.mantissa(), price.exponent(), frac);

	}

	// note: can overflow
	public static final long priceWhole(/* local */long mantissa, /* local */
			int exponent, final Fraction frac) {

		final int scale = frac.decimalExponent;

		while (exponent > scale) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < scale) {
			mantissa /= 10;
			exponent++;
		}

		return mantissa / frac.decimalDenominator;

	}

	// note: can overflow
	public static final long priceFraction(/* local */long mantissa, /* local */
			int exponent, /* local */Fraction frac) {

		if (frac == null) {
			frac = Fraction.DEC_Z00;
		}

		final int scale = frac.decimalExponent;

		while (exponent > scale) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < scale) {
			mantissa /= 10;
			exponent++;
		}

		mantissa = abs(mantissa);

		switch (frac.base) {
		default:
		case DECIMAL:
			mantissa %= frac.denominator;
			break;
		case BINARY:
			mantissa %= frac.decimalDenominator;
			mantissa *= frac.denominator;
			mantissa /= frac.decimalDenominator;
			break;
		}

		return mantissa;

	}

	public static final String priceWholeText(/* local */PriceValue price) {
		if (price == null) {
			price = ValueConst.NULL_PRICE;
		}
		return ""; // priceWholeText(price.mantissa(), price.exponent());
	}

	public static final String priceFractionText(
	/* local */PriceValue price,
	/* local */Fraction frac) {

		if (price == null) {
			price = ValueConst.NULL_PRICE;
		}

		if (frac == null) {
			frac = Fraction.DEC_Z00;
		}

		long value = priceFraction(price, frac);

		final char[] array = new char[frac.places];

		for (int k = frac.places - 1; k >= 0; k--) {
			final char alpha = (char) (value % 10 + '0');
			value /= 10;
			array[k] = alpha;
		}

		return new String(array);

	}

//	/**
//	 * render price according to BARCHART convention & fraction format; decimal
//	 * vs binary; number of places;
//	 */
//	public static final String priceText(/* local */PriceValue price,
//	/* local */Fraction frac) {
//
//		if (price == null) {
//			price = ValueConst.NULL_PRICE;
//		}
//
//		if (frac == null) {
//			frac = Fraction.DEC_Z00;
//		}
//
//		return priceText(price.mantissa(), price.exponent(), frac);
//
//	}
	
	public static final String priceText(PriceValue price, final long base, final long exponent) {
		
		if (price == null) {
			price = ValueConst.NULL_PRICE;
		}
		
		return priceText(price.mantissa(), price.exponent(), base, exponent);
		
	}

	/**
	 * render price according to BARCHART convention & fraction format; decimal
	 * vs binary; proper number of places;
	 */
	public static final String priceText(long mantissa, int exponent, long base, long baseExp) {

		final long denominator = base ^ baseExp;
		
		/* consume sign */

		final boolean isMinus;

		if (mantissa < 0) {
			mantissa = -mantissa;
			isMinus = true;
		} else {
			isMinus = false;
		}

		/* normalize exponent to fraction */

		final long scale = baseExp;

		while (exponent > scale) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < scale) {
			mantissa /= 10;
			exponent++;
		}

		/* produce whole */

		long whole = mantissa / base;

		/* produce part */

		final char separator;

		if(base == 2) {
			mantissa %= base;
			separator = ASCII.DOT;
		} else if(base == 10) {
			mantissa %= base;
			mantissa *= denominator;
			mantissa /= base;
			separator = ASCII.DASH;
		} else {
			separator = 'X';
		}
		
//		switch (base) {
//		default:
//		case DECIMAL:
//			mantissa %= frac.denominator;
//			separator = ASCII.DOT;
//			break;
//		case BINARY:
//			mantissa %= frac.decimalDenominator;
//			mantissa *= frac.denominator;
//			mantissa /= frac.decimalDenominator;
//			separator = ASCII.DASH;
//			break;
//		}

		long part = mantissa;

		/* render price to array */

		final int size = 64;

		final char[] array = new char[size];

		int index = size - 1;

		long places = 0;
		if(base == 2) {
			if(denominator == 1) {
				places = 0;
			} else {
				places = (int) (1 + log10(denominator));
			}
		} else if(base == 10) {
			places = -baseExp;
		}

		/* part */if (places > 0) {

			for (int k = 0; k < places; k++) {
				final char alpha = (char) (part % 10 + ASCII._0_);
				array[index--] = alpha;
				part /= 10;
			}

			array[index--] = separator;

		}

		/* whole */if (whole == 0) {
			array[index--] = ASCII._0_;
		} else {

			while (whole != 0) {
				final char alpha = (char) (whole % 10 + ASCII._0_);
				array[index--] = alpha;
				whole /= 10;
			}

			if (isMinus) {
				array[index--] = ASCII.MINUS;
			}

		}

		final int offset = index + 1;

		final int length = size - offset;

		return new String(array, offset, length);

	}

	/**
	 * BARCHART convention: use ',' separators regardless of user LOCALE
	 */
	public static final String sizeText(/* local */SizeValue size) {
		if (size == null) {
			size = ValueConst.NULL_SIZE;
		}
		return sizeText(size.asLong());
	}

	/**
	 * BARCHART convention: use ',' separators regardless of user LOCALE
	 * 
	 * TODO : replace String.format with direct render
	 */
	public static final String sizeText(final long size) {
		return String.format("%,d", size);
	}

	/** in default time zone */
	public static final String timeTextISO(final TimeValue value) {
		final long millisUTC = value.asMillisUTC();
		final DateTime time = new DateTime(millisUTC);
		return time.toString();
	}

	private final static DateTimeFormatter TIME_SHORT = DateTimeFormat
			.forPattern("HH:mm:ss");

	private final static DateTimeFormatter DATE_SHORT = DateTimeFormat
			.forPattern("MM/dd/yyyy");

	private final static DateTimeFormatter TIME_WITH_DATE = DateTimeFormat
			.forPattern("MM/dd/yyyy HH:mm:ss");

	/** in default time zone */
	public static final String timeTextShort(final TimeValue value) {
		return TIME_SHORT.print(value.asMillisUTC());
	}

	/** in default time zone with date MM/dd/yyyy */
	public static final String dateTimeText(final TimeValue value) {
		return TIME_WITH_DATE.print(value.asMillisUTC());
	}

	/** in default date MM/dd/yyyy */
	public static final String dateTextShort(final TimeValue value) {
		return DATE_SHORT.print(value.asMillisUTC());
	}

	/** in provided time zone */
	public static final String timeTextShort(final TimeValue value,
			final DateTimeZone zone) {
		return TIME_SHORT.print(value.asDateTime(zone));
	}

//	/** in instrument time zone */
//	public static final String timeTextShort(final TimeValue value,
//			final Instrument instrument) {
//		final TextValue name = instrument.get(InstrumentField.TIME_ZONE);
//		final DateTimeZone zone = DateTimeZone.forID(name.toString());
//		return TIME_SHORT.print(value.asDateTime(zone));
//	}

	// ############################################

	/** fraction display format base */
	public static enum Base {

		NULL_BASE(10), //

		BINARY(2), // i.e. 5/32
		DECIMAL(10), // i.e. 0.127

		;

		public final int value;

		Base(final int base) {
			this.value = base;
		}

		public static final Base fromValue(final int value) {
			switch (value) {
			case 2:
				return BINARY;
			case 10:
				return DECIMAL;
			default:
				return NULL_BASE;
			}
		}

		public final boolean isBinary() {
			return this == BINARY;
		}

		public final boolean isDecimal() {
			return this == DECIMAL;
		}

	}

	// ############################################

	/** known decimal or binary exponent */
	public static enum Exponent {

		NULL_EXPONENT(0), //

		P12(+12), //
		P11(+11), //
		P10(+10), //
		P09(+9), //
		P08(+8), //
		P07(+7), //
		P06(+6), //
		P05(+5), //
		P04(+4), //
		P03(+3), //
		P02(+2), //
		P01(+1), //
		Z00(0), //
		N01(-1), //
		N02(-2), //
		N03(-3), //
		N04(-4), //
		N05(-5), //
		N06(-6), //
		N07(-7), //
		N08(-8), //
		N09(-9), //
		N10(-10), //
		N11(-11), //
		N12(-12), //

		;

		public final int value;

		Exponent(final int exponent) {
			this.value = exponent;
		}

		public static final Exponent fromValue(final int value) {
			for (final Exponent known : ENUM_VALUES) {
				if (known.value == value) {
					return known;
				}
			}
			return NULL_EXPONENT;
		}

		private static final Exponent[] ENUM_VALUES = values();

	}

	// ############################################

	/** known fraction descriptor */
	public static enum Fraction implements Value<Fraction> {

		NULL_FRACTION(Base.NULL_BASE, Exponent.NULL_EXPONENT,
				"unknown fraction"), //

		DEC_Z00(DECIMAL, Z00, "decimal, zero exponent, 1.0"), //

		DEC_N01(DECIMAL, N01, "decimal, negative one, 0.1"), //
		DEC_N02(DECIMAL, N02, "decimal, negative two, 0.01"), //
		DEC_N03(DECIMAL, N03, "decimal, negative three, 0.001"), //
		DEC_N04(DECIMAL, N04, "decimal, negative four, 0.000,1"), //
		DEC_N05(DECIMAL, N05, "decimal, negative five, 0.000,01"), //
		DEC_N06(DECIMAL, N06, "decimal, negative six, 0.000,001"), //
		DEC_N07(DECIMAL, N07, "decimal, negative seven, 0.000,000,1"), //
		DEC_N08(DECIMAL, N08, "decimal, negative eight, 0.000,000,01"), //
		DEC_N09(DECIMAL, N09, "decimal, negative nine, 0.000,000,001"), //

		BIN_Z00(BINARY, Z00, "binary, zero exponent, 1/1"), //

		BIN_N01(BINARY, N01, "binary, negative one, 1/2"), //
		BIN_N02(BINARY, N02, "binary, negative two, 1/4"), //
		BIN_N03(BINARY, N03, "binary, negative three, 1/8"), //
		BIN_N04(BINARY, N04, "binary, negative four, 1/16"), //
		BIN_N05(BINARY, N05, "binary, negative five, 1/32"), //
		BIN_N06(BINARY, N06, "binary, negative six, 1/64"), //
		BIN_N07(BINARY, N07, "binary, negative seven, 1/128"), //
		BIN_N08(BINARY, N08, "binary, negative eight, 1/256"), //
		BIN_N09(BINARY, N09, "binary, negative nine, 1/512"), //

		;

		public String description;

		public final Base base;
		public final Exponent exponent;

		/** native form, such as 1/10 or 1/32 */
		public final long numerator;
		public final long denominator;

		/** decimal form, such as 1/10 */
		public final int decimalExponent;
		public final long decimalDenominator;

		/** number of PLACES it takes to DISPLAY a fraction in a native form */
		public final int places;

		Fraction(final Base base, final Exponent exponent,
				final String description) {

			this.base = base;
			this.exponent = exponent;
			this.description = description;

			numerator = 1;
			denominator = (long) pow(base.value, -exponent.value);

			decimalExponent = exponent.value;
			decimalDenominator = (long) pow(10, -decimalExponent);

			switch (base) {
			case BINARY:
				if (denominator == 1) {
					places = 0;
				} else {
					places = (int) (1 + log10(denominator));
				}
				break;
			default:
			case DECIMAL:
				places = -exponent.value;
				break;
			}

		}

		public final boolean isSmallerThan(final Fraction that) {
			if (that == null) {
				return false;
			}
			return this.decimalExponent < that.decimalExponent;
		}

		@SuppressWarnings("unused")
		private static final Fraction[] ENUM_VALUES = values();

		@Override
		public Fraction freeze() {
			return this;
		}

		@Override
		public boolean isFrozen() {
			return true;
		}

		@Override
		public boolean isNull() {
			return this == NULL_FRACTION;
		}

	}

	// ############################################

}
