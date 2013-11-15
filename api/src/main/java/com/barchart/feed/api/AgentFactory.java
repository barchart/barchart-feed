package com.barchart.feed.api;

import com.barchart.feed.api.model.data.MarketData;

public interface AgentFactory {
	
	<V extends MarketData<V>> Agent newAgent(final Class<V> clazz,
			final MarketObserver<V> callback);

}
