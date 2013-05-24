package com.barchart.feed.api.core;

import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;

/*
 * AKA MarketBase
 */
public interface Marketplace {

	<V extends MarketData> Agent newAgent(MarketCallback<V> callback, 
			MarketEventType... types);
}
