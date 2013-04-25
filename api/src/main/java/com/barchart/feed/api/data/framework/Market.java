package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.client.MarketObject;
import com.barchart.feed.api.market.FrameworkAgent;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.missive.api.TagMap;

public interface Market extends MarketObject, TagMap, Comparable<Market> {

	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
