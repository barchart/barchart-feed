/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.fields;

import com.barchart.feed.api.Schedule;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.inst.GuidList;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeInterval;

public interface InstrumentField {

	/** guid object used by feed for instrument identity*/
	Tag<InstrumentGUID> GUID = Tag.create(InstrumentGUID.class);
	
	/** market identifier; must be globally unique; */
	Tag<TextValue> MARKET_GUID = Tag.create(TextValue.class);

	/** type of security, Forex, Equity, etc. */
	Tag<SecurityType> SECURITY_TYPE = Tag.create(SecurityType.class);
	
	/** liquidy type, default / implied / combined */
	Tag<BookLiquidityType> BOOK_LIQUIDITY = Tag.create(BookLiquidityType.class);

	/** structure of book  */
	Tag<BookStructureType> BOOK_STRUCTURE = Tag.create(BookStructureType.class);
	
	/** book depth */
	Tag<SizeValue> BOOK_DEPTH = Tag.create(SizeValue.class);
	
	/** vendor */
	Tag<TextValue> VENDOR = Tag.create(TextValue.class);

	/** market symbol; can be non unique; */
	Tag<TextValue> SYMBOL = Tag.create(TextValue.class);

	/** market free style description; can be used in full text search */
	Tag<TextValue> DESCRIPTION = Tag.create(TextValue.class);
	
	/** stock vs future vs etc. */
	Tag<TextValue> CFI_CODE = Tag.create(TextValue.class);
	
	/** price currency */
	Tag<MarketCurrency> CURRENCY_CODE = Tag.create(MarketCurrency.class);

	/** market originating exchange identifier */
	Tag<TextValue> EXCHANGE_CODE = Tag.create(TextValue.class);

	/** price step / increment size / tick size */
	Tag<PriceValue> PRICE_STEP = Tag.create(PriceValue.class);

	/** value of a future contract / stock share */
	Tag<PriceValue> POINT_VALUE = Tag.create(PriceValue.class);

	Tag<Fraction> DISPLAY_FRACTION = Tag.create(Fraction.class);
	
	/** lifetime of instrument */
	Tag<TimeInterval> LIFETIME = Tag.create(TimeInterval.class);

	/**
	 * array of intervals of market hours in a normal week, milliseconds from
	 * Sunday 00:00:00.000, local market time
	 */
	Tag<Schedule> MARKET_HOURS = Tag.create(Schedule.class);

	/**
	 * time zone represented as offset in milliseconds between local market time
	 * and UTC
	 */
	Tag<SizeValue> TIME_ZONE_OFFSET = Tag.create(SizeValue.class);

	/** time zone name as text */
	Tag<TextValue> TIME_ZONE_NAME = Tag.create(TextValue.class);

	/** ordered list of component guids */
	public static final Tag<GuidList> COMPONENT_LEGS = Tag
			.create(GuidList.class);

	// keep last
	Tag<?>[] FIELDS = Tag.collectTop(InstrumentField.class);

}
