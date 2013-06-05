package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.SessionType;

public interface Market extends MarketData<Market> {

	<V extends MarketData> V get(MarketData.Type type);
	
	Market market();
	
	Instrument instrument();
	
	Trade lastTrade();

	OrderBook orderBook();

	PriceLevel lastBookUpdate();

	TopOfBook topOfBook();

	Cuvol cuvol();
	
	Session session(SessionType type);

}
