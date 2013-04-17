/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static java.lang.Math.log10;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.barchart.feed.api.framework.market.MarketDisplay;
import com.barchart.util.ascii.ASCII;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueBuilder;
import com.barchart.util.values.provider.ValueConst;

public class MarketDisplayBaseImpl implements MarketDisplay {
	
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
	
	private final static DateTimeFormatter TIME_SHORT = DateTimeFormat
			.forPattern("HH:mm:ss");

	private final static DateTimeFormatter DATE_SHORT = DateTimeFormat
			.forPattern("MM/dd/yyyy");

	private final static DateTimeFormatter TIME_WITH_DATE = DateTimeFormat
			.forPattern("MM/dd/yyyy HH:mm:ss");

	@Override
	public String timeYearFull(TimeValue time) {
		time = filter(time);
		return YEAR_FULL.print(time.asMillisUTC());
	}

	@Override
	public String timeYearShort(TimeValue time) {
		time = filter(time);
		return YEAR_SHORT.print(time.asMillisUTC());
	}

	@Override
	public String timeMonthFull(TimeValue time) {
		time = filter(time);
		return MONTH_FULL.print(time.asMillisUTC());
	}

	@Override
	public String timeMonthShort(TimeValue time) {
		time = filter(time);
		return MONTH_SHORT.print(time.asMillisUTC()).toUpperCase();
	}

	@Override
	public String timeMonthCode(TimeValue time) {
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

	@Override
	public String priceFractionText(PriceValue price, Fraction frac) {
		
		if (price == null) {
			price = ValueConst.NULL_PRICE;
		}
		
		if(frac == null || frac.isNull()) {
			frac = ValueBuilder.newFraction(10, 0);
		}
		
		long value = frac.priceFraction(price);
		
		final char[] array = new char[frac.places()];
		
		for(int k = frac.places() - 1; k >= 0; k--) {
			final char alpha = (char) (value % 10 + '0');
			value /= 10;
			array[k] = alpha;
		}
		
		return new String(array);
	}
	
	@Override
	public String priceText(PriceValue price, Fraction frac) {
		if(price == null) {
			price = ValueConst.NULL_PRICE;
		}
		if(frac == null) {
			//TODO
		}
		
		long mantissa = price.mantissa();
		int exponent = price.exponent();
		boolean isMinus;
		
		if (mantissa < 0) {
			mantissa = -mantissa;
			isMinus = true;
		} else {
			isMinus = false;
		}
		
		final long scale = frac.exponent();

		while (exponent > scale) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < scale) {
			mantissa /= 10;
			exponent++;
		}
		
		long whole = frac.priceWhole(mantissa, exponent);
		
		final char separator;
		if(frac.base() == 10) {
			mantissa %= frac.denominator();
			separator = ASCII.DOT;
		} else {
			mantissa %= frac.decimalDenominator();
			mantissa *= frac.denominator();
			mantissa /= frac.decimalDenominator();
			separator = ASCII.DASH;			
		} 
		
		long part = mantissa;
		
		final int size = 64;

		final char[] array = new char[size];

		int index = size - 1;

		long places = frac.places();
		if(frac.base() == 10) {
			places--;
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

	
	@Override
	public String priceText(PriceValue price, long base, long exponent) {
		return priceText(price.mantissa(), price.exponent(), base, exponent);
	}

	@Override
	public String priceText(long mantissa, int exponent, long base, long baseExp) {
		
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

	@Override
	public String sizeText(SizeValue size) {
		if (size == null) {
			size = ValueConst.NULL_SIZE;
		}
		return sizeText(size.asLong());
	}

	@Override
	public String sizeText(long size) {
		return String.format("%,d", size);
	}

	@Override
	public String timeTextISO(TimeValue value) {
		final long millisUTC = value.asMillisUTC();
		final DateTime time = new DateTime(millisUTC);
		return time.toString();
	}

	@Override
	public String timeTextShort(TimeValue value) {
		return TIME_SHORT.print(value.asMillisUTC());
	}

	@Override
	public String dateTimeText(TimeValue value) {
		return TIME_WITH_DATE.print(value.asMillisUTC());
	}

	@Override
	public String dateTextShort(TimeValue value) {
		return DATE_SHORT.print(value.asMillisUTC());
	}

	@Override
	public String timeTextShort(TimeValue value, DateTimeZone zone) {
		return TIME_SHORT.print(value.asDateTime(zone));
	}

}
