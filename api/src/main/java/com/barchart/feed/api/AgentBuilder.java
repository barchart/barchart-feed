package com.barchart.feed.api;

import com.barchart.feed.api.model.MarketData;

/** FIXME not a uilder */
public interface AgentBuilder {

	<V extends MarketData<V>> Agent newAgent(Class<V> clazz,
			MarketObserver<V> callback);

}
