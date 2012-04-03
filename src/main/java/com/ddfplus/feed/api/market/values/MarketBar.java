package com.ddfplus.feed.api.market.values;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketBarField;

/**
 * represents a market bar, such as O-H-L-C;
 * 
 * bar can be for current or previous day;
 */
public interface MarketBar extends Value<MarketBar> {

	<V extends Value<V>> V get(MarketBarField<V> field);

}
