/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.market.api.MarketDisplay;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.impl.FactoryImpl;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.TimeValue;

public class TestMarketDisplayBaseImpl {
	
	private static final Factory factory = new FactoryImpl();
	
	public static MarketDisplay display = new MarketDisplayBaseImpl();
	
	public static Fraction BIN_Z00 = factory.newFraction(2, 0);
	public static Fraction BIN_N01 = factory.newFraction(2, -1);
	public static Fraction BIN_N02 = factory.newFraction(2, -2);
	public static Fraction BIN_N03 = factory.newFraction(2, -3);
	public static Fraction BIN_N04 = factory.newFraction(2, -4);
	public static Fraction BIN_N07 = factory.newFraction(2, -7);
	
	public static Fraction DEC_Z00 = factory.newFraction(10, 0);
	public static Fraction DEC_N01 = factory.newFraction(10, -1);
	public static Fraction DEC_N02 = factory.newFraction(10, -2);
	public static Fraction DEC_N03 = factory.newFraction(10, -3);
	public static Fraction DEC_N04 = factory.newFraction(10, -4);
	public static Fraction DEC_N05 = factory.newFraction(10, -5);
	public static Fraction DEC_N06 = factory.newFraction(10, -6);
	public static Fraction DEC_N07 = factory.newFraction(10, -7);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPriceFractionString() {
		
		Fraction frac;
		PriceValue price;
		long fraction;
		String string;
		
		frac = BIN_N01;
		price = newPrice(123500, -3);
		fraction = frac.priceFraction(price.mantissa(), price.exponent());
		assertEquals(1, fraction); // 1/2
		string = display.priceFractionText(price, frac);
		assertEquals("1", string); // 1/2

		frac = BIN_N02;
		price = newPrice(123500, -3);
		fraction = frac.priceFraction(price.mantissa(), price.exponent());
		assertEquals(2, fraction); // 2/4
		string = display.priceFractionText(price, frac);
		assertEquals("2", string); // 2/4

		frac = BIN_N03;
		price = newPrice(123500, -3);
		fraction = frac.priceFraction(price.mantissa(), price.exponent());
		assertEquals(4, fraction); // 4/8
		string = display.priceFractionText(price, frac);
		assertEquals("4", string); // 4/8

		frac = BIN_N04;
		price = newPrice(123500, -3);
		fraction = frac.priceFraction(price.mantissa(), price.exponent());
		assertEquals(8, fraction); // 8/16
		string = display.priceFractionText(price, frac);
		assertEquals("08", string); // 08/16

		frac = BIN_N07;
		price = newPrice(123500, -3);
		fraction = frac.priceFraction(price.mantissa(), price.exponent());
		assertEquals(64, fraction); // 64/128
		string = display.priceFractionText(price, frac);
		assertEquals("064", string); // 64/128
		
	}
	
	@Test
	public void testPriceWhole() {
		
		PriceValue price;
		long whole;

		price = newPrice(1236998923, -6);
		whole = DEC_Z00.priceWhole(price.mantissa(), price.exponent());
		assertEquals(whole, 1236);
		
		price = newPrice(-1236998923, -6);
		whole = DEC_Z00.priceWhole(price.mantissa(), price.exponent());
		assertEquals(whole, -1236);
		
		price = newPrice(233469923, +5);
		whole = DEC_Z00.priceWhole(price.mantissa(), price.exponent());
		assertEquals(whole, 23346992300000L);
		
		price = newPrice(-233469923, +5);
		whole = DEC_Z00.priceWhole(price.mantissa(), price.exponent());
		assertEquals(whole, -23346992300000L);
		
	}
	
	@Test
	public void testTimeShort() {
		
		final long millisUTC = new DateTime("2012-01-04T12:13:14.123Z")
		.getMillis();

		final TimeValue value = newTime(millisUTC);

		final String text = display.timeTextShort(value);

		// 	6 am in CST is 12 noon UTC in January 2012

		assertEquals(text, "06:13:14");

		System.out.println("time : " + text);

	}
	
	@Test
	public void testTimeShortZone1() {
		
		final long millisUTC = new DateTime("2012-01-04T12:13:14.123Z")
		.getMillis();

		final TimeValue value = newTime(millisUTC);

		final DateTimeZone zone = DateTimeZone.forID("America/New_York");

		final String text = display.timeTextShort(value, zone);

		// 7 am in EST is 12 noon UTC in January 2012

		assertEquals(text, "07:13:14");

		System.out.println("time : " + text);
		
	}
	
	@Test
	public void testTimeShortZone2() {
		
		final long millisUTC = new DateTime("2012-01-04T12:13:14.123Z")
			.getMillis();

		final TimeValue value = newTime(millisUTC);
		
		final DateTimeZone zone = DateTimeZone.forOffsetHours(-5);
		
		final String text = display.timeTextShort(value, zone);
		
		// 7 am in EST is 12 noon UTC in January 2012
		
		assertEquals(text, "07:13:14");
		
		System.out.println("time : " + text);
		
	}
	
	@Test
	public void testPriceText() {
		
		PriceValue price;
		String text;
		
		//
		price = newPrice(181925, -2);
		text = display.priceText(price, DEC_N02);
		System.out.println("price : " + text);
		assertEquals("1819.25", text);

		//
		price = newPrice(181900, -2);
		text = display.priceText(price, DEC_N02);
		System.out.println("price : " + text);
		assertEquals("1819.00", text);

		// 1819 1/4
		price = newPrice(181925, -2);
		text = display.priceText(price, BIN_N02);
		System.out.println("price : " + text);
		assertEquals("1819-1", text);

		// 1819 3/4
		price = newPrice(181975, -2);
		text = display.priceText(price, BIN_N02);
		System.out.println("price : " + text);
		assertEquals("1819-3", text);

		// 1819 1/8
		price = newPrice(1819125, -3);
		text = display.priceText(price, BIN_N03);
		System.out.println("price : " + text);
		assertEquals("1819-1", text);

		// 1819 2/8
		price = newPrice(1819250, -3);
		text = display.priceText(price, BIN_N03);
		System.out.println("price : " + text);
		assertEquals("1819-2", text);

		// 1819 2/16
		price = newPrice(18191250, -4);
		text = display.priceText(price, BIN_N04);
		System.out.println("price : " + text);
		assertEquals("1819-02", text);

		// 2 3/128
		price = newPrice(20234375, -7);
		text = display.priceText(price, BIN_N07);
		System.out.println("price : " + text);
		assertEquals("2-003", text);

		price = newPrice(181925, -3);
		text = display.priceText(price, DEC_N04);
		System.out.println("price : " + text);
		assertEquals("181.9250", text);

		price = newPrice(181925, -2);
		text = display.priceText(price, DEC_N04);
		System.out.println("price : " + text);
		assertEquals("1819.2500", text);

		price = newPrice(1819, 0);
		text = display.priceText(price, DEC_N03);
		System.out.println("price : " + text);
		assertEquals("1819.000", text);

		price = newPrice(-1819, 0);
		text = display.priceText(price, DEC_N03);
		System.out.println("price : " + text);
		assertEquals("-1819.000", text);

		price = newPrice(18195, -1);
		text = display.priceText(price, DEC_N02);
		System.out.println("price : " + text);
		assertEquals("1819.50", text);

		price = newPrice(-1, 0);
		text = display.priceText(price, DEC_Z00);
		System.out.println("price : " + text);
		assertEquals("-1", text);

		price = newPrice(-1, 0);
		text = display.priceText(price, DEC_N02);
		System.out.println("price : " + text);
		assertEquals("-1.00", text);

		price = newPrice(-125, -2);
		text = display.priceText(price, DEC_N02);
		System.out.println("price : " + text);
		assertEquals("-1.25", text);

		// 1 3/4
		price = newPrice(175, -2);
		text = display.priceText(price, BIN_N02);
		System.out.println("price : " + text);
		assertEquals("1-3", text);

		// -1 3/4
		price = newPrice(-175, -2);
		text = display.priceText(price, BIN_N02);
		System.out.println("price : " + text);
		assertEquals("-1-3", text);
		
	}
	
	@Test
	public void testMonthFull() {
		
		final TimeValue time = newTime(0);
		final String text = display.timeMonthFull(time);
		System.out.println("month : >" + text + "<");
		
	}
	
	@Test
	public void testMonthShort() {
		
		final TimeValue time = newTime(0);
		final String text = display.timeMonthShort(time);
		System.out.println("month : >" + text + "<");
		
	}
	
}
