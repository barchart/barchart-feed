package com.barchart.feed.api.consumer;

import com.barchart.feed.api.consumer.data.MarketData;
import com.barchart.feed.api.consumer.enums.MarketEventType;

/*
 * AKA MarketBase
 */
public interface Marketplace {

	<V extends MarketData> Agent newAgent(MarketCallback<V> callback, 
			MarketEventType... types);
}
