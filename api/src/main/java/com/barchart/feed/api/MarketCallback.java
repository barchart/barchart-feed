package com.barchart.feed.api;

import com.barchart.feed.api.data.MarketData;

public interface MarketCallback<V extends MarketData<V>> {
	
	void call(V v);

}
