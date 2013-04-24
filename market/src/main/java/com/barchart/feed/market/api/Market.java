package com.barchart.feed.market.api;

import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.feed.api.market.data.MarketObject;

public interface Market extends MarketObject {

	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
	void attach(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
