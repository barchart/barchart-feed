package com.barchart.feed.api;

import com.barchart.feed.api.model.data.MarketData;

public interface MarketObserver<V extends MarketData<V>> {
	
	void onNext(V v);

}
