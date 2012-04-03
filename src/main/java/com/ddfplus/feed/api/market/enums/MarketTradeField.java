package com.ddfplus.feed.api.market.enums;

import static com.barchart.util.values.provider.ValueConst.*;
import static com.ddfplus.feed.api.market.enums.MarketBarType.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.enums.DictEnum;
import com.barchart.util.enums.ParaEnumBase;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.common.util.collections.BitSetEnum;

@NotMutable
public final class MarketTradeField<V extends Value<V>> extends
		ParaEnumBase<V, MarketTradeField<V>> implements
		BitSetEnum<MarketTradeField<?>> {

	// ##################################

	/** where trade originated */
	public static final MarketTradeField<MarketBarType> TYPE = NEW(NULL_BAR_TYPE);

	/** last trade price */
	public static final MarketTradeField<PriceValue> PRICE = NEW(NULL_PRICE);

	/** last trade size */
	public static final MarketTradeField<SizeValue> SIZE = NEW(NULL_SIZE);

	/** last trade time */
	public static final MarketTradeField<TimeValue> TRADE_TIME = NEW(NULL_TIME);

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

	private static <X extends Value<X>> MarketTradeField<X> NEW(X defaultValue) {
		return new MarketTradeField<X>(defaultValue);
	}

}
