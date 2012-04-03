package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.ThreadSafe;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.common.util.collections.FastEnumMap;

@ThreadSafe
class FieldMap<T> extends FastEnumMap<MarketField<?>, T> {

	private static final MarketField<?>[] FIELDS = MarketField.values();

	FieldMap() {
		super(FIELDS);
	}

}
