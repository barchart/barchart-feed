package com.barchart.feed.api.market;

import org.joda.time.DateTimeZone;

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
	
	/** render price in fraction format */
	String priceText(PriceValue price, final long base, final long exponent);
	
	/** render price in fraction format */
	String priceText(long mantissa, int exponent, long base, long baseExp);
	
	/**
	 * BARCHART convention: use ',' separators regardless of user LOCALE
	 */
	String sizeText(SizeValue size);
	
	/**
	 * BARCHART convention: use ',' separators regardless of user LOCALE
	 */
	String sizeText(final long size);
	
	/** in default time zone */
	String timeTextISO(final TimeValue value);
	
	/** in default time zone */
	String timeTextShort(final TimeValue value);
	
	/** in default time zone with date MM/dd/yyyy */
	String dateTimeText(final TimeValue value);
	
	/** in default date MM/dd/yyyy */
	String dateTextShort(final TimeValue value);
	
	/** in provided time zone */
	String timeTextShort(final TimeValue value,	final DateTimeZone zone);
	
	
	
}
