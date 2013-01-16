package com.barchart.feed.inst.enums;

import static com.barchart.feed.base.instrument.api.InstrumentConst.NULL_CURRENCY;
import static com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction.NULL_FRACTION;
import static com.barchart.util.values.provider.ValueConst.NULL_PRICE;
import static com.barchart.util.values.provider.ValueConst.NULL_SIZE;
import static com.barchart.util.values.provider.ValueConst.NULL_TEXT;
import static com.barchart.util.values.provider.ValueConst.NULL_TIME;

import com.barchart.feed.base.book.enums.MarketBookType;
import com.barchart.feed.base.instrument.enums.CodeCFI;
import com.barchart.feed.base.instrument.enums.MarketCurrency;
import com.barchart.feed.base.instrument.enums.MarketDisplay.Fraction;
import com.barchart.util.collections.BitSetEnum;
import com.barchart.util.enums.DictEnum;
import com.barchart.util.enums.ParaEnumBase;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;

public final class InstrumentDefField<V extends Value<V>> extends
	ParaEnumBase<V, InstrumentDefField<V>> implements
	BitSetEnum<InstrumentDefField<?>> {

	/** market identifier; must be globally unique; */
	public static final InstrumentDefField<TextValue> ID = NEW(NULL_TEXT);

	/** market security group identifier */
	public static final InstrumentDefField<TextValue> GROUP_ID = NEW(NULL_TEXT);

	/** market originating exchange identifier */
	public static final InstrumentDefField<TextValue> EXCHANGE_ID = NEW(NULL_TEXT);

	/** market symbol; can be non unique; */
	public static final InstrumentDefField<TextValue> SYMBOL = NEW(NULL_TEXT);

	/** market free style description; can be used in full text search */
	public static final InstrumentDefField<TextValue> DESCRIPTION = NEW(NULL_TEXT);

	/** book depth */
	public static final InstrumentDefField<SizeValue> BOOK_SIZE = NEW(NULL_SIZE);

	/** book management type; default vs implied vs combined */
	public static final InstrumentDefField<MarketBookType> BOOK_TYPE = NEW(MarketBookType.DEFAULT);

	/** price step / increment size / tick size */
	public static final InstrumentDefField<PriceValue> PRICE_STEP = NEW(NULL_PRICE);

	/** value of a future contract / stock share */
	public static final InstrumentDefField<PriceValue> PRICE_POINT = NEW(NULL_PRICE);

	/** display fraction render format : decimal vs binary, etc. */
	public static final InstrumentDefField<Fraction> FRACTION = NEW(NULL_FRACTION);

	/** price currency */
	public static final InstrumentDefField<MarketCurrency> CURRENCY = NEW(NULL_CURRENCY);

	/** stock vs future vs etc. */
	public static final InstrumentDefField<CodeCFI> TYPE = NEW(CodeCFI.UNKNOWN);
	
	/**
	 * market instrument local time zone; can be used to render time in market's
	 * local time;
	 * 
	 * http://joda-time.sourceforge.net/timezones.html
	 */
	public static final InstrumentDefField<TextValue> TIME_ZONE = NEW(NULL_TEXT);

	/** market open time on normal business day */
	public static final InstrumentDefField<TimeValue> TIME_OPEN = NEW(NULL_TIME);

	/** market close time on normal business day */
	public static final InstrumentDefField<TimeValue> TIME_CLOSE = NEW(NULL_TIME);

	/** instrument initiation trading date; such as option issue date */
	public static final InstrumentDefField<TimeValue> DATE_START = NEW(NULL_TIME);

	/** instrument termination trading date; such as future expiration month */
	public static final InstrumentDefField<TimeValue> DATE_FINISH = NEW(NULL_TIME);
	
	public static int size() {
		return values().length;
	}

	public static InstrumentDefField<?>[] values() {
		return DictEnum.valuesForType(InstrumentDefField.class);
	}

	private final long mask;

	@Override
	public long mask() {
		return mask;
	}
	
	private InstrumentDefField() {
		super();
		mask = 0;
	}
	
	private InstrumentDefField(final V value) {
		super("", value);
		mask = ONE << ordinal();
	}
	
	private static final <X extends Value<X>> InstrumentDefField<X> NEW(X value) {
		return new InstrumentDefField<X>(value);
	}
	
}
