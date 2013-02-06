/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.enums;

import static com.barchart.feed.inst.enums.MarketDisplay.priceFraction;
import static com.barchart.feed.inst.enums.MarketDisplay.priceFractionText;
import static com.barchart.feed.inst.enums.MarketDisplay.priceText;
import static com.barchart.feed.inst.enums.MarketDisplay.timeMonthFull;
import static com.barchart.feed.inst.enums.MarketDisplay.timeMonthShort;
import static com.barchart.feed.inst.enums.MarketDisplay.timeTextShort;
import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueBuilder.newTime;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.api.fields.InstrumentField;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.feed.inst.provider.InstrumentFactory;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueBuilder;

public class TestMarketDisplay {

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

		frac = Fraction.BIN_N01;
		price = newPrice(123500, -3);
		fraction = priceFraction(price, frac);
		assertEquals(1, fraction); // 1/2
		string = priceFractionText(price, frac);
		assertEquals("1", string); // 1/2

		frac = Fraction.BIN_N02;
		price = newPrice(123500, -3);
		fraction = priceFraction(price, frac);
		assertEquals(2, fraction); // 2/4
		string = priceFractionText(price, frac);
		assertEquals("2", string); // 2/4

		frac = Fraction.BIN_N03;
		price = newPrice(123500, -3);
		fraction = priceFraction(price, frac);
		assertEquals(4, fraction); // 4/8
		string = priceFractionText(price, frac);
		assertEquals("4", string); // 4/8

		frac = Fraction.BIN_N04;
		price = newPrice(123500, -3);
		fraction = priceFraction(price, frac);
		assertEquals(8, fraction); // 8/16
		string = priceFractionText(price, frac);
		assertEquals("08", string); // 08/16

		frac = Fraction.BIN_N07;
		price = newPrice(123500, -3);
		fraction = priceFraction(price, frac);
		assertEquals(64, fraction); // 64/128
		string = priceFractionText(price, frac);
		assertEquals("064", string); // 64/128

	}

	@Test
	public void testPriceFraction() {

		PriceValue price;
		long fraction;

		price = newPrice(123500, -3);
		fraction = priceFraction(price, Fraction.BIN_N01);
		assertEquals(1, fraction); // 1/2

		price = newPrice(-123500, -3);
		fraction = priceFraction(price, Fraction.BIN_N01);
		assertEquals(1, fraction); // 1/2

		price = newPrice(123125, -3);
		fraction = priceFraction(price, Fraction.BIN_N03);
		assertEquals(1, fraction); // 1/8

		price = newPrice(1230250999, -6);
		fraction = priceFraction(price, Fraction.BIN_N03);
		assertEquals(2, fraction); // 2/8, approximately

		price = newPrice(123375, -3);
		fraction = priceFraction(price, Fraction.BIN_N03);
		assertEquals(3, fraction); // 3/8

		price = newPrice(12325, -2);
		fraction = priceFraction(price, Fraction.BIN_N02);
		assertEquals(1, fraction); // 1/4

		price = newPrice(12300, -2);
		fraction = priceFraction(price, Fraction.BIN_N02);
		assertEquals(0, fraction); // 0/4

		price = newPrice(1231875, -4);
		fraction = priceFraction(price, Fraction.DEC_N04);
		assertEquals(1875, fraction); // 3/16

		price = newPrice(1231875, -4);
		fraction = priceFraction(price, Fraction.BIN_N04);
		assertEquals(3, fraction); // 3/16

		price = newPrice(1237777, -4);
		fraction = priceFraction(price, Fraction.BIN_N04);
		assertEquals(12, fraction); // 12/16 approximately

		price = newPrice(999523437500L, -9);
		fraction = priceFraction(price, Fraction.BIN_N07);
		assertEquals(67, fraction); // 67/128

		price = newPrice(-999523437500L, -9);
		fraction = priceFraction(price, Fraction.BIN_N07);
		assertEquals(67, fraction); // 67/128

		price = newPrice(1, 0);
		fraction = priceFraction(price, Fraction.BIN_Z00);
		assertEquals(0, fraction); // 0/1

		//

		price = newPrice(1236998923, -6);
		fraction = priceFraction(price, Fraction.DEC_N06);
		assertEquals(fraction, 998923);

		price = newPrice(1236998923, -6);
		fraction = priceFraction(price, Fraction.DEC_N03);
		assertEquals(fraction, 998);

		price = newPrice(1236998923777L, -9);
		fraction = priceFraction(price, Fraction.DEC_N03);
		assertEquals(fraction, 998); // truncated

		price = newPrice(1236998923, 3);
		fraction = priceFraction(price, Fraction.DEC_N03);
		assertEquals(fraction, 0);

		price = newPrice(1, 0);
		fraction = priceFraction(price, Fraction.DEC_N03);
		assertEquals(fraction, 0);

		price = newPrice(1, 0);
		fraction = priceFraction(price, Fraction.DEC_Z00);
		assertEquals(fraction, 0);

		price = newPrice(-1, 0);
		fraction = priceFraction(price, Fraction.DEC_Z00);
		assertEquals(fraction, 0);

		//

	}

	@Test
	public void testMarketFraction() {

		for (final Fraction fraction : Fraction.values()) {
			System.out.println("" + fraction + " " + fraction.numerator + " "
					+ fraction.denominator + " spaces=" + fraction.places
					+ " conExp=" + fraction.decimalExponent);
		}
	}

	@Test
	public void testPriceWhole() {

		final PriceValue price;
		final long whole;

		// price = newPrice(1236998923, -6);
		// whole = priceWhole(price);
		// assertEquals(whole, 1236);
		//
		// price = newPrice(-1236998923, -6);
		// whole = priceWhole(price);
		// assertEquals(whole, -1236);
		//
		// price = newPrice(233469923, +5);
		// whole = priceWhole(price);
		// assertEquals(whole, 23346992300000L);
		//
		// price = newPrice(-233469923, +5);
		// whole = priceWhole(price);
		// assertEquals(whole, -23346992300000L);

	}

	@Test
	public void testTimeShort() {

		final long millisUTC = new DateTime("2012-01-04T12:13:14.123Z")
				.getMillis();

		final TimeValue value = newTime(millisUTC);

		final String text = timeTextShort(value);

		// 6 am in CST is 12 noon UTC in January 2012

		assertEquals(text, "06:13:14");

		System.out.println("time : " + text);

	}

	@Test
	public void testTimeShortZone1() {

		final long millisUTC = new DateTime("2012-01-04T12:13:14.123Z")
				.getMillis();

		final TimeValue value = newTime(millisUTC);

		final DateTimeZone zone = DateTimeZone.forID("America/New_York");

		final String text = timeTextShort(value, zone);

		// 7 am in EST is 12 noon UTC in January 2012

		assertEquals(text, "07:13:14");

		System.out.println("time : " + text);

	}

	@Test
	public void testTimeShortZone2() {

		final long millisUTC = new DateTime("2012-01-04T12:13:14.123Z")
				.getMillis();

		final TimeValue value = newTime(millisUTC);

		final Map<Tag, Object> map = new HashMap<Tag, Object>();
		map.put(InstrumentField.TIME_ZONE_OFFSET, ValueBuilder.newSize(60 * -5));
		map.put(InstrumentField.MARKET_GUID, ValueBuilder.newText("1"));
		final Instrument inst = InstrumentFactory.build(map);
		final DateTimeZone zone = DateTimeZone.forOffsetHours(-5);
		
		final String text = timeTextShort(value, zone);

		// 7 am in EST is 12 noon UTC in January 2012

		assertEquals(text, "07:13:14");

		System.out.println("time : " + text);

	}

	@Test
	public void testPriceText() {

		PriceValue price;
		String text;

//		//
//		price = newPrice(181925, -2);
//		text = priceText(price, Fraction.DEC_N02);
//		System.out.println("price : " + text);
//		assertEquals("1819.25", text);
//
//		//
//		price = newPrice(181900, -2);
//		text = priceText(price, Fraction.DEC_N02);
//		System.out.println("price : " + text);
//		assertEquals("1819.00", text);
//
//		// 1819 1/4
//		price = newPrice(181925, -2);
//		text = priceText(price, Fraction.BIN_N02);
//		System.out.println("price : " + text);
//		assertEquals("1819-1", text);
//
//		// 1819 3/4
//		price = newPrice(181975, -2);
//		text = priceText(price, Fraction.BIN_N02);
//		System.out.println("price : " + text);
//		assertEquals("1819-3", text);
//
//		// 1819 1/8
//		price = newPrice(1819125, -3);
//		text = priceText(price, Fraction.BIN_N03);
//		System.out.println("price : " + text);
//		assertEquals("1819-1", text);
//
//		// 1819 2/8
//		price = newPrice(1819250, -3);
//		text = priceText(price, Fraction.BIN_N03);
//		System.out.println("price : " + text);
//		assertEquals("1819-2", text);
//
//		// 1819 2/16
//		price = newPrice(18191250, -4);
//		text = priceText(price, Fraction.BIN_N04);
//		System.out.println("price : " + text);
//		assertEquals("1819-02", text);
//
//		// 2 3/128
//		price = newPrice(20234375, -7);
//		text = priceText(price, Fraction.BIN_N07);
//		System.out.println("price : " + text);
//		assertEquals("2-003", text);
//
//		price = newPrice(181925, -3);
//		text = priceText(price, Fraction.DEC_N04);
//		System.out.println("price : " + text);
//		assertEquals("181.9250", text);
//
//		price = newPrice(181925, -2);
//		text = priceText(price, Fraction.DEC_N04);
//		System.out.println("price : " + text);
//		assertEquals("1819.2500", text);
//
//		price = newPrice(1819, 0);
//		text = priceText(price, Fraction.DEC_N03);
//		System.out.println("price : " + text);
//		assertEquals("1819.000", text);
//
//		price = newPrice(-1819, 0);
//		text = priceText(price, Fraction.DEC_N03);
//		System.out.println("price : " + text);
//		assertEquals("-1819.000", text);
//
//		price = newPrice(18195, -1);
//		text = priceText(price, Fraction.DEC_N02);
//		System.out.println("price : " + text);
//		assertEquals("1819.50", text);
//
//		price = newPrice(-1, 0);
//		text = priceText(price, Fraction.DEC_Z00);
//		System.out.println("price : " + text);
//		assertEquals("-1", text);
//
//		price = newPrice(-1, 0);
//		text = priceText(price, Fraction.DEC_N02);
//		System.out.println("price : " + text);
//		assertEquals("-1.00", text);
//
//		price = newPrice(-125, -2);
//		text = priceText(price, Fraction.DEC_N02);
//		System.out.println("price : " + text);
//		assertEquals("-1.25", text);
//
//		// 1 3/4
//		price = newPrice(175, -2);
//		text = priceText(price, Fraction.BIN_N02);
//		System.out.println("price : " + text);
//		assertEquals("1-3", text);
//
//		// -1 3/4
//		price = newPrice(-175, -2);
//		text = priceText(price, Fraction.BIN_N02);
//		System.out.println("price : " + text);
//		assertEquals("-1-3", text);

	}

	@Test
	public void testMonthFull() {

		final TimeValue time = newTime(0);
		final String text = timeMonthFull(time);
		System.out.println("month : >" + text + "<");

	}

	@Test
	public void testMonthShort() {

		final TimeValue time = newTime(0);
		final String text = timeMonthShort(time);
		System.out.println("month : >" + text + "<");

	}

}
