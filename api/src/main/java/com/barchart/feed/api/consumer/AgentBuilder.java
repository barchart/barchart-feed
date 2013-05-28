package com.barchart.feed.api.consumer;

import com.barchart.feed.api.consumer.data.MarketData;
import com.barchart.feed.api.consumer.enums.MarketEventType;

/*
 * AKA MarketBase
 */
public interface AgentBuilder {

	<V extends MarketData> Agent newAgent(Class<V> clazz, MarketCallback<V> callback, 
			MarketEventType... types);
}
