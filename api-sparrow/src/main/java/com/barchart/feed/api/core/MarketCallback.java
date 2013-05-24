package com.barchart.feed.api.core;

import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;

public interface MarketCallback<V extends MarketData> {
	
	void call(V v, MarketEventType type);

}
