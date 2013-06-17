package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;

public interface Market extends MarketData<Market> {

	Trade lastTrade();

	OrderBook orderBook();

	Cuvol cuvol();
	
	Session session();

}
