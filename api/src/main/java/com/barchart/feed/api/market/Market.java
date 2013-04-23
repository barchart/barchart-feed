package com.barchart.feed.api.market;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.data.MarketObject;

public interface Market extends MarketObject {

	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
}
