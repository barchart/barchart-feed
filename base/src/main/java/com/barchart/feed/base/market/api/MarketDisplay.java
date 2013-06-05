package com.barchart.feed.base.market.api;

import org.joda.time.DateTimeZone;

import com.barchart.util.value.api.Fraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public interface MarketDisplay {
	
	/** such as 2011 */
	String timeYearFull(TimeValue time);

	/** such as 2011 = 11 */
	String timeYearShort(TimeValue time);

	/** such as December */
	String timeMonthFull(TimeValue time);

	/** such as December = DEC */
	String timeMonthShort(TimeValue time);

	/** such as future January = "F" */
	String timeMonthCode(TimeValue time);

	String priceFractionText(PriceValue price, Fraction frac);

	/** render price in fraction format */
	String priceText(PriceValue price, Fraction frac);

	/** render price in fraction format */
	String priceText(PriceValue price, long base, long exponent);

	/** render price in fraction format */
	String priceText(long mantissa, int exponent, long base, long baseExp);

	/**
	 * BARCHART convention: use ',' separators regardless of user LOCALE
	 */
	String sizeText(SizeValue size);

	/**
	 * BARCHART convention: use ',' separators regardless of user LOCALE
	 */
	String sizeText(long size);

	/** in default time zone */
	String timeTextISO(TimeValue value);

	/** in default time zone */
	String timeTextShort(TimeValue value);

	/** in default time zone with date MM/dd/yyyy */
	String dateTimeText(TimeValue value);

	/** in default date MM/dd/yyyy */
	String dateTextShort(TimeValue value);

	/** in provided time zone */
	String timeTextShort(TimeValue value, DateTimeZone zone); 

}
