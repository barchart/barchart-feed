package com.barchart.feed.api;

import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.util.Observer;

public interface MarketObserver<V extends MarketData<V>> extends Observer<V> {
	
	void onNext(V v);

}
