package com.barchart.feed.api.core;

import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;

/*
 * Will extends base missive class Callback<V extends TagMap>
 */
public interface MarketCallback<V extends MarketData> {
	
	void call(V v, MarketEventType type);

}
