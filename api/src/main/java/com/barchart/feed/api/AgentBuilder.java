package com.barchart.feed.api;

import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;

public interface AgentBuilder {

	<V extends MarketData<V>> Agent newAgent(MarketData.Type clazz, 
			MarketCallback<V> callback,	MarketEventType... types);
	
}
