/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.market.enums;

import static com.barchart.util.values.provider.ValueConst.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.collections.BitSetEnum;
import com.barchart.util.enums.DictEnum;
import com.barchart.util.enums.ParaEnumBase;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;

@NotMutable
/** represents O-H-L-C market bar fields */
public final class MarketBarField<V extends Value<V>> extends
		ParaEnumBase<V, MarketBarField<V>> implements
		BitSetEnum<MarketBarField<?>> {

	// ##################################

	// prices

	public static final MarketBarField<PriceValue> OPEN = NEW(NULL_PRICE);
	public static final MarketBarField<PriceValue> HIGH = NEW(NULL_PRICE);
	public static final MarketBarField<PriceValue> LOW = NEW(NULL_PRICE);
	public static final MarketBarField<PriceValue> CLOSE = NEW(NULL_PRICE);
	public static final MarketBarField<PriceValue> SETTLE = NEW(NULL_PRICE);

	// sizes

	public static final MarketBarField<SizeValue> VOLUME = NEW(NULL_SIZE);
	public static final MarketBarField<SizeValue> INTEREST = NEW(NULL_SIZE);

	/** last time of any bar field change */
	public static final MarketBarField<TimeValue> BAR_TIME = NEW(NULL_TIME);

	/**
	 * latest known market trading date represented by this bar
	 * 
	 * expressed as UTC zone
	 * 
	 * year, month, day should be treated as local market trade date
	 */
	public static final MarketBarField<TimeValue> TRADE_DATE = NEW(NULL_TIME);

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
