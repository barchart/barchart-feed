package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotThreadSafe;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.common.util.collections.FastEnumSet;

@NotThreadSafe
class FieldSet extends FastEnumSet<MarketField<?>> {

	private static final MarketField<?>[] FIELDS = MarketField.values();

	FieldSet() {
		super(FIELDS);
	}

}
