package com.barchart.feed.api;

import com.barchart.feed.api.data.MarketData;

public interface AgentBuilder {

	<V extends MarketData<V>> Agent newAgent(Class<V> clazz, 
			MarketCallback<V> callback);
	
}
