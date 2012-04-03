package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketTradeField;
import com.ddfplus.feed.api.market.values.MarketTrade;

interface MarketDoTrade extends MarketTrade {

	<V extends Value<V>> void set(final MarketTradeField<V> field, final V value);

}
