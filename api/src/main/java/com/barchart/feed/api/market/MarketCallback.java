package com.barchart.feed.api.market;

/*
 * Will extends base missive class Callback<V extends TagMap>
 */
public interface MarketCallback<V extends MarketDataObject<V>> {
	
	void call(V v);

}
