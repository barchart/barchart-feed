/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.enums;

import static com.barchart.feed.base.instrument.api.InstrumentConst.*;
import static com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction.*;
import static com.barchart.util.values.provider.ValueConst.*;

import com.barchart.feed.base.book.enums.MarketBookType;
import com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.collections.BitSetEnum;
import com.barchart.util.enums.DictEnum;
import com.barchart.util.enums.ParaEnumBase;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueConst;

@NotMutable
public final class InstrumentField<V extends Value<V>> extends
		ParaEnumBase<V, InstrumentField<V>> implements
		BitSetEnum<InstrumentField<?>> {

	// ##################################

	/** market identifier; must be globally unique; */
	public static final InstrumentField<TextValue> ID = NEW(NULL_TEXT);

	/** market security group identifier */
	public static final InstrumentField<TextValue> GROUP_ID = NEW(NULL_TEXT);

	/** market originating exchange identifier */
	public static final InstrumentField<TextValue> EXCHANGE_ID = NEW(NULL_TEXT);

	/** market symbol; can be non unique; */
	public static final InstrumentField<TextValue> SYMBOL = NEW(NULL_TEXT);
	
	public static final InstrumentField<TextValue> CQG_TRADING_SYMBOL = NEW(NULL_TEXT);

	/** market free style description; can be used in full text search */
	public static final InstrumentField<TextValue> DESCRIPTION = NEW(NULL_TEXT);

	/** book depth */
	public static final InstrumentField<SizeValue> BOOK_SIZE = NEW(NULL_SIZE);

	/** book management type; default vs implied vs combined */
	public static final InstrumentField<MarketBookType> BOOK_TYPE = NEW(MarketBookType.DEFAULT);

	/** price step / increment size / tick size */
	public static final InstrumentField<PriceValue> PRICE_STEP = NEW(NULL_PRICE);

	/** value of a future contract / stock share */
	public static final InstrumentField<PriceValue> PRICE_POINT = NEW(NULL_PRICE);

	/** display fraction render format : decimal vs binary, etc. */
	public static final InstrumentField<Fraction> FRACTION = NEW(MarketDisplay.Fraction.NULL_FRACTION);

	/** price currency */
	public static final InstrumentField<MarketCurrency> CURRENCY = NEW(NULL_CURRENCY);

	/** stock vs future vs etc. */
	public static final InstrumentField<CodeCFI> TYPE = NEW(CodeCFI.UNKNOWN);

	// ##################################

	// /** TODO */
	// public static final InstrumentField<MarketCalendar> CALENDAR =
	// NEW(NULL_CALENDAR);

	/**
	 * market instrument local time zone; can be used to render time in market's
	 * local time;
	 * 
	 * http://joda-time.sourceforge.net/timezones.html
	 */
	public static final InstrumentField<TextValue> TIME_ZONE = NEW(NULL_TEXT);

	/** market open time on normal business day */
	public static final InstrumentField<TimeValue> TIME_OPEN = NEW(NULL_TIME);

	/** market close time on normal business day */
	public static final InstrumentField<TimeValue> TIME_CLOSE = NEW(NULL_TIME);

	/** instrument initiation trading date; such as option issue date */
	public static final InstrumentField<TimeValue> DATE_START = NEW(NULL_TIME);

	/** instrument termination trading date; such as future expiration month */
	public static final InstrumentField<TimeValue> DATE_FINISH = NEW(NULL_TIME);

	// ##################################

	public static int size() {
		return values().length;
	}

	public static InstrumentField<?>[] values() {
		return DictEnum.valuesForType(InstrumentField.class);
	}

	private final long mask;

	@Override
	public long mask() {
		return mask;
	}

	private InstrumentField() {
		super();
		mask = 0;
	}

	private InstrumentField(final V value) {
		super("", value);
		mask = ONE << ordinal();
	}

	private static final <X extends Value<X>> InstrumentField<X> NEW(X value) {
		return new InstrumentField<X>(value);
	}

}
