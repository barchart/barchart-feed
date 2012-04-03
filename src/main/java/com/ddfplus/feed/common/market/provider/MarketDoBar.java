package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketBarField;
import com.ddfplus.feed.api.market.values.MarketBar;

public interface MarketDoBar extends MarketBar {

	<V extends Value<V>> void set(final MarketBarField<V> field, final V value);

}
