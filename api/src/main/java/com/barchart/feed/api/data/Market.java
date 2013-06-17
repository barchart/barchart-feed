package com.barchart.feed.api.data;

public interface Market extends MarketData<Market> {

	Market market();
	
	Trade lastTrade();

	OrderBook orderBook();

	PriceLevel lastBookUpdate();

	TopOfBook topOfBook();

	Cuvol cuvol();
	
	Session session();

}
