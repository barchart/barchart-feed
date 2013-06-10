package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.SessionType;

public interface Market extends MarketData<Market> {

	Market market();
	
	Instrument instrument();
	
	Trade lastTrade();

	OrderBook orderBook();

	PriceLevel lastBookUpdate();

	TopOfBook topOfBook();

	Cuvol cuvol();
	
	// TODO
	Session session(SessionType type);

}
