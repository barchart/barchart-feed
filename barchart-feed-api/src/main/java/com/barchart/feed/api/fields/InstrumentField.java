package com.barchart.feed.api.fields;

import com.barchart.feed.api.enums.BookLiquidity;
import com.barchart.feed.api.enums.BookStructure;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeInterval;

public interface InstrumentField {

	/** market identifier; must be globally unique; */
	Tag<TextValue> MARKET_ID = Tag.create(TextValue.class);

	/** vendor */
	Tag<TextValue> VENDOR = Tag.create(TextValue.class);

	/** market symbol; can be non unique; */
	Tag<TextValue> SYMBOL = Tag.create(TextValue.class);

	/** market free style description; can be used in full text search */
	Tag<TextValue> DESCRIPTION = Tag.create(TextValue.class);

	/** market originating exchange identifier */
	Tag<TextValue> EXCHANGE_CODE = Tag.create(TextValue.class);

	/** book depth */
	Tag<SizeValue> BOOK_DEPTH = Tag.create(SizeValue.class);

	/** stock vs future vs etc. */
	Tag<TextValue> CFI_CODE = Tag.create(TextValue.class);

	/** price currency */
	Tag<MarketCurrency> CURRENCY = Tag.create(MarketCurrency.class);

	/** price step / increment size / tick size */
	Tag<PriceValue> PRICE_STEP = Tag.create(PriceValue.class);

	/** value of a future contract / stock share */
	Tag<PriceValue> POINT_VALUE = Tag.create(PriceValue.class);

	/** display fraction base : decimal(10) vs binary(2), etc. */
	Tag<SizeValue> DISPLAY_BASE = Tag.create(SizeValue.class);

	/** display fraction exponent */
	Tag<SizeValue> DISPLAY_EXPONENT = Tag.create(SizeValue.class);

	/** lifetime of instrument */
	Tag<TimeInterval> LIFETIME = Tag.create(TimeInterval.class);

	/**
	 * array of intervals of market hours in a normal week, milliseconds from
	 * Sunday 00:00:00.000, local market time
	 */
	Tag<TimeInterval[]> MARKET_HOURS = Tag.create(TimeInterval[].class);

	/**
	 * time zone represented as offset in milliseconds between local market time
	 * and UTC
	 * <p>
	 * FIXME offset is seasonal
	 */
	Tag<SizeValue> TIME_ZONE = Tag.create(SizeValue.class);

	/** FIXME migrate */
	Tag<BookLiquidity> BOOK_LIQUIDITY = Tag.create(BookLiquidity.class);

	/** FIXME migrate */
	Tag<BookStructure> BOOK_STRUCTURE = Tag.create(BookStructure.class);

	/** ordered list of component guids */
	public static final Tag<SizeValue[]> COMPONENT_LEGS = Tag
			.create(SizeValue[].class);

	// keep last
	Tag<?>[] FIELDS = Tag.collectTop(InstrumentField.class);

}
