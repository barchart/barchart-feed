package com.barchart.feed.api.consumer;

import com.barchart.feed.api.consumer.data.MarketData;
import com.barchart.feed.api.consumer.enums.MarketEventType;

public interface MarketCallback<V extends MarketData> {
	
	void call(V v, MarketEventType type);

}
