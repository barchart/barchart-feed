package com.barchart.feed.api.core;

import com.barchart.feed.api.data.MarketData;

/*
 * Will extends base missive class Callback<V extends TagMap>
 */
public interface MarketCallback<V extends MarketData> {
	
	void call(V v);

}
