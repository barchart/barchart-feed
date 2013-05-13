package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.MarketData;

/*
 * Will extends base missive class Callback<V extends TagMap>
 */
public interface MarketCallback<V extends MarketData> {
	
	void call(V v);

}
