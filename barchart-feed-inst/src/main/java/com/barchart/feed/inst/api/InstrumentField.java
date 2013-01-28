package com.barchart.feed.inst.api;

import com.barchart.feed.inst.enums.CodeCFI;
import com.barchart.feed.inst.enums.MarketBookType;
import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.feed.inst.enums.MarketDisplay.Fraction;
import com.barchart.missive.core.Tag;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;

public final class InstrumentField {
	
	private InstrumentField() {
		
	}
	
	/** market identifier; must be globally unique; */
	public static final Tag<TextValue> ID = Tag.create("ID", TextValue.class);
	
	/** market security group identifier */
	public static final Tag<TextValue> GROUP_ID = Tag.create("GROUP_ID", TextValue.class);
	
	/** market originating exchange identifier */
	public static final Tag<TextValue> EXCHANGE_ID = Tag.create("EXCHANGE_ID", TextValue.class);
	
	/** market symbol; can be non unique; */
	public static final Tag<TextValue> SYMBOL = Tag.create("SYMBOL", TextValue.class);
	
	/** market free style description; can be used in full text search */
	public static final Tag<TextValue> DESCRIPTION = Tag.create("DESCRIPTION", TextValue.class);
	
	/** book depth */
	public static final Tag<SizeValue> BOOK_SIZE = Tag.create("BOOK_SIZE", SizeValue.class);
	
	/** book management type; default vs implied vs combined */
	public static final Tag<MarketBookType> BOOK_TYPE = Tag.create("BOOK_TYPE", MarketBookType.class);
	
	/** price step / increment size / tick size */
	public static final Tag<PriceValue> PRICE_STEP = Tag.create("PRICE_STEP", PriceValue.class);
	
	/** value of a future contract / stock share */
	public static final Tag<PriceValue> PRICE_POINT = Tag.create("PRICE_POINT", PriceValue.class);
	
	/** display fraction render format : decimal vs binary, etc. */
	public static final Tag<Fraction> FRACTION = Tag.create("FRACTION", Fraction.class);
	
	/** price currency */
	public static final Tag<MarketCurrency> CURRENCY = Tag.create("CURRENCY", MarketCurrency.class);
	
	/** stock vs future vs etc. */
	public static final Tag<CodeCFI> TYPE = Tag.create("TYPE", CodeCFI.class);
	
	/**
	 * market instrument local time zone; can be used to render time in market's
	 * local time;
	 * 
	 * http://joda-time.sourceforge.net/timezones.html
	 */
	public static final Tag<TextValue> TIME_ZONE = Tag.create("TIME_ZONE", TextValue.class);
	
	/** market open time on normal business day */
	public static final Tag<TimeValue> TIME_OPEN = Tag.create("TIME_OPEN", TimeValue.class);
	
	/** market close time on normal business day */
	public static final Tag<TimeValue> TIME_CLOSE = Tag.create("TIME_CLOSE", TimeValue.class);
	
	/** instrument initiation trading date; such as option issue date */
	public static final Tag<TimeValue> DATE_START = Tag.create("DATE_START", TimeValue.class);
	
	/** instrument termination trading date; such as future expiration month */
	public static final Tag<TimeValue> DATE_FINISH = Tag.create("DATE_FINISH", TimeValue.class);
	
	public static final Tag<?>[] FIELDS = new Tag[] {
		ID, GROUP_ID, EXCHANGE_ID, SYMBOL, DESCRIPTION, BOOK_SIZE, BOOK_TYPE, PRICE_STEP,
		PRICE_POINT, FRACTION, CURRENCY, TYPE, TIME_ZONE, TIME_OPEN, TIME_CLOSE,
		DATE_START, DATE_FINISH
	};

}
