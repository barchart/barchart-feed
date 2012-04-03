package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketTradeField;

/** represents market trade transaction */
@NotMutable
public interface MarketTrade extends Value<MarketTrade> {

	<V extends Value<V>> V get(MarketTradeField<V> field);

}
