package com.barchart.feed.api.consumer.data;

import com.barchart.feed.api.consumer.enums.SessionType;

public interface Market extends MarketData {

	Instrument instrument();
	
	Trade lastTrade();

	OrderBook orderBook();

	PriceLevel lastBookUpdate();

	TopOfBook topOfBook();

	Cuvol cuvol();

	Session session(SessionType type);

}
