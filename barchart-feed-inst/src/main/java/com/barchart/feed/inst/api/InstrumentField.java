package com.barchart.feed.inst.api;

import com.barchart.feed.inst.enums.CodeCFI;
import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;

public interface InstrumentField {

	/** market identifier; must be globally unique; */
	Tag<SizeValue> GUID = Tag.create("ID", SizeValue.class);

	/** vendor */
	Tag<TextValue> VENDOR = Tag.create("VENDOR", TextValue.class);

	/** market symbol; can be non unique; */
	Tag<TextValue> VENDOR_SYMBOL = Tag.create("SYMBOL", TextValue.class);

	/** market free style description; can be used in full text search */
	Tag<TextValue> DESCRIPTION = Tag.create("DESCRIPTION", TextValue.class);

	/** market originating exchange identifier */
	Tag<TextValue> EXCHANGE_ID = Tag.create("EXCHANGE_ID", TextValue.class);

	/** book depth */
	Tag<SizeValue> BOOK_DEPTH = Tag.create("BOOK_SIZE", SizeValue.class);

	/** stock vs future vs etc. */
	Tag<CodeCFI> CFI_CODE = Tag.create("TYPE", CodeCFI.class);

	/** price currency */
	Tag<MarketCurrency> CURRENCY = Tag.create("CURRENCY", MarketCurrency.class);

	/** price step / increment size / tick size */
	Tag<PriceValue> PRICE_STEP = Tag.create("PRICE_STEP", PriceValue.class);

	/** value of a future contract / stock share */
	Tag<PriceValue> POINT_VALUE = Tag.create("PRICE_POINT", PriceValue.class);

	/** display fraction base : decimal(10) vs binary(2), etc. */
	Tag<SizeValue> DISPLAY_BASE = Tag.create("DISPLAY_BASE", SizeValue.class);

	/** display fraction exponent */
	Tag<SizeValue> DISPLAY_EXPONENT = Tag.create("DISPLAY_EXPONENT",
			SizeValue.class);

	/** lifetime of instrument */
	Tag<TimeInterval> LIFETIME = Tag.create("LIFETIME", TimeInterval.class);

	/**
	 * array of intervals of market hours in a normal week, denoted in minutes
	 * from Sunday morning
	 */
	Tag<TimeInterval[]> MARKET_HOURS = Tag.create("MARKET_HOURS",
			TimeInterval[].class);

	/** timezone represented as offset in minutes from utc */
	Tag<SizeValue> TIME_ZONE_OFFSET = Tag.create("TIME_ZONE", SizeValue.class);

	Tag<?>[] FIELDS = new Tag[] { GUID, VENDOR, VENDOR_SYMBOL, EXCHANGE_ID,
			DESCRIPTION, BOOK_DEPTH, CFI_CODE, CURRENCY, PRICE_STEP,
			POINT_VALUE, DISPLAY_BASE, DISPLAY_EXPONENT, LIFETIME,
			MARKET_HOURS, TIME_ZONE_OFFSET };

}
