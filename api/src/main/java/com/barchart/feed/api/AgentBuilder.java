package com.barchart.feed.api;

import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;

public interface AgentBuilder {

	// throw new runtime exception on bad class 
	<V extends MarketData<V>> Agent newAgent(Class<V> clazz, 
			MarketCallback<V> callback,	MarketEventType... types);
	
}
