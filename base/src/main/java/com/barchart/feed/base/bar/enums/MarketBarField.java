/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.bar.enums;

import static com.barchart.feed.base.values.provider.ValueConst.NULL_BOOLEAN;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_PRICE;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_SIZE;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_TIME;

import com.barchart.feed.base.collections.BitSetEnum;
import com.barchart.feed.base.enums.DictEnum;
import com.barchart.feed.base.enums.ParaEnumBase;
import com.barchart.feed.base.values.api.BooleanValue;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
/** represents O-H-L-C market bar fields */
public final class MarketBarField<V extends Value<V>> 
		extends ParaEnumBase<V, MarketBarField<V>> 
		implements BitSetEnum<MarketBarField<?>> {

	// ##################################

	// prices

	public static final MarketBarField<PriceValue> OPEN = NEW(NULL_PRICE); // 0
	public static final MarketBarField<PriceValue> HIGH = NEW(NULL_PRICE); // 1
	public static final MarketBarField<PriceValue> LOW = NEW(NULL_PRICE);  // 2
	public static final MarketBarField<PriceValue> CLOSE = NEW(NULL_PRICE); // 3
	public static final MarketBarField<PriceValue> SETTLE = NEW(NULL_PRICE); // 4
	public static final MarketBarField<BooleanValue> IS_SETTLED = NEW(NULL_BOOLEAN); // 5

	public static final MarketBarField<PriceValue> CLOSE_PREVIOUS = NEW(NULL_PRICE); // 6

	// sizes

	public static final MarketBarField<SizeValue> VOLUME = NEW(NULL_SIZE); // 7
	public static final MarketBarField<SizeValue> INTEREST = NEW(NULL_SIZE); // 8

	/** last time of any bar field change */
	public static final MarketBarField<TimeValue> BAR_TIME = NEW(NULL_TIME); // 9

	/**
	 * latest known market trading date represented by this bar
	 *
	 * expressed as UTC zone
	 *
	 * year, month, day should be treated as local market trade date
	 */
	public static final MarketBarField<TimeValue> TRADE_DATE = NEW(NULL_TIME); // 10
	
	public static final MarketBarField<PriceValue> VWAP = NEW(NULL_PRICE); // 11

	// ##################################

	public static int size() {
		return values().length;
	}

	public static MarketBarField<?>[] values() {
		return DictEnum.valuesForType(MarketBarField.class);
	}

	private final long mask;

	@Override
	public long mask() {
		return mask;
	}

	private MarketBarField() {
		super();
		mask = 0;
	}

	private MarketBarField(final V value) {
		super("", value);
		mask = ONE << ordinal();
	}

	private static <X extends Value<X>> MarketBarField<X> NEW(
			final X defaultValue) {
		return new MarketBarField<X>(defaultValue);
	}

}
