/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.inst.provider.InstrumentGUID;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.TagFactory;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;

public interface InstrumentField {

	/** guid object used by feed for instrument identity*/
	Tag<InstrumentGUID> GUID = TagFactory.create(InstrumentGUID.class);
	
	/** market identifier; must be globally unique; */
	Tag<TextValue> MARKET_GUID = TagFactory.create(TextValue.class);

	/** type of security, Forex, Equity, etc. */
	Tag<Instrument.SecurityType> SECURITY_TYPE = TagFactory.create(Instrument.SecurityType.class);
	
	/** liquidy type, default / implied / combined */
	Tag<Instrument.BookLiquidityType> BOOK_LIQUIDITY = TagFactory.create(Instrument.BookLiquidityType.class);

	/** structure of book  */
	Tag<Instrument.BookStructureType> BOOK_STRUCTURE = TagFactory.create(Instrument.BookStructureType.class);
	
	/** book depth */
	Tag<SizeValue> BOOK_DEPTH = TagFactory.create(SizeValue.class);
	
	/** vendor */
	Tag<TextValue> VENDOR = TagFactory.create(TextValue.class);

	/** market symbol; can be non unique; */
	Tag<TextValue> SYMBOL = TagFactory.create(TextValue.class);

	/** market free style description; can be used in full text search */
	Tag<TextValue> DESCRIPTION = TagFactory.create(TextValue.class);
	
	/** stock vs future vs etc. */
	Tag<TextValue> CFI_CODE = TagFactory.create(TextValue.class);
	
	/** exchange data object */
	Tag<Exchange> EXCHANGE = TagFactory.create(Exchange.class);
	
	//TODO Remove, replaced with above tag
	/** market originating exchange identifier */
	Tag<TextValue> EXCHANGE_CODE = TagFactory.create(TextValue.class);

	/** price step / increment size / tick size */
	Tag<PriceValue> TICK_SIZE = TagFactory.create(PriceValue.class);

	/** value of a future contract / stock share */
	Tag<PriceValue> POINT_VALUE = TagFactory.create(PriceValue.class);

	Tag<Fraction> DISPLAY_FRACTION = TagFactory.create(Fraction.class);
	
	/** lifetime of instrument */
	Tag<TimeInterval> LIFETIME = TagFactory.create(TimeInterval.class);

	/**
	 * array of intervals of market hours in a normal week, milliseconds from
	 * Sunday 00:00:00.000, local market time
	 */
	Tag<Schedule> MARKET_HOURS = TagFactory.create(Schedule.class);

	/**
	 * time zone represented as offset in milliseconds between local market time
	 * and UTC
	 */
	Tag<SizeValue> TIME_ZONE_OFFSET = TagFactory.create(SizeValue.class);

	/** time zone name as text */
	Tag<TextValue> TIME_ZONE_NAME = TagFactory.create(TextValue.class);

	// keep last
	Tag<?>[] FIELDS = TagFactory.collectTop(InstrumentField.class);

}
