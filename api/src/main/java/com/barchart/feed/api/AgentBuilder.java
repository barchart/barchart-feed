package com.barchart.feed.api;

import com.barchart.feed.api.model.data.MarketData;

/** FIXME not a builder */
public interface AgentBuilder {

	<V extends MarketData<V>> Agent newAgent(Class<V> clazz,
			MarketObserver<V> callback);

}
