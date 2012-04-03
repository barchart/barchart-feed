package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketField;

/** represents complete market */
@NotMutable
public interface Market extends Value<Market> {

	<V extends Value<V>> V get(MarketField<V> field);

}
