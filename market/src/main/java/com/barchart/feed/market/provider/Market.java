package com.barchart.feed.market.provider;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.data.MarketObject;
import com.barchart.feed.api.market.MarketMessage;

public interface Market extends MarketObject {

	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
}
