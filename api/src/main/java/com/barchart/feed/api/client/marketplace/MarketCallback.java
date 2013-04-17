package com.barchart.feed.api.client.marketplace;

import com.barchart.feed.api.client.data.MarketObject;

/*
 * Will extends base missive class Callback<V extends TagMap>
 */
public interface MarketCallback<V extends MarketObject> {
	
	void call(V v);

}
