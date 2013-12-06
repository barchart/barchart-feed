/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.enums;

import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.NULL_TRADE_SEQUENCE;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.NULL_TRADE_SESSION;
import static com.barchart.feed.base.trade.enums.MarketTradeType.NULL_TRADE_TYPE;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_PRICE;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_SIZE;
import static com.barchart.feed.base.values.provider.ValueConst.NULL_TIME;

import com.barchart.feed.base.collections.BitSetEnum;
import com.barchart.feed.base.enums.DictEnum;
import com.barchart.feed.base.enums.ParaEnumBase;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
public final class MarketTradeField<V extends Value<V>> extends
		ParaEnumBase<V, MarketTradeField<V>> implements
		BitSetEnum<MarketTradeField<?>> {

	// ##################################

	/** where trade originated */
	public static final MarketTradeField<MarketTradeType> TYPE = NEW(NULL_TRADE_TYPE);

	/** where trade originated */
	public static final MarketTradeField<MarketTradeSession> SESSION = NEW(NULL_TRADE_SESSION);

	/** where trade originated */
	public static final MarketTradeField<MarketTradeSequencing> SEQUENCING = NEW(NULL_TRADE_SEQUENCE);

	/** last trade price */
	public static final MarketTradeField<PriceValue> PRICE = NEW(NULL_PRICE);

	/** last trade size */
	public static final MarketTradeField<SizeValue> SIZE = NEW(NULL_SIZE);

	/** last trade time */
	public static final MarketTradeField<TimeValue> TRADE_TIME = NEW(NULL_TIME);

	/** last trade day/session */
	public static final MarketTradeField<TimeValue> TRADE_DATE = NEW(NULL_TIME);

	// ##################################

	public static int size() {
		return values().length;
	}

	public static MarketTradeField<?>[] values() {
		return DictEnum.valuesForType(MarketTradeField.class);
	}

	private final long mask;

	@Override
	public long mask() {
		return mask;
	}

	private MarketTradeField() {
		super();
		mask = 0;
	}

	private MarketTradeField(final V value) {
		super("", value);
		mask = ONE << ordinal();
	}

	private static <X extends Value<X>> MarketTradeField<X> NEW(
			final X defaultValue) {
		return new MarketTradeField<X>(defaultValue);
	}

}
