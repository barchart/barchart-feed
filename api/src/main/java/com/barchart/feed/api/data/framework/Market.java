package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.client.MarketObject;
import com.barchart.feed.api.market.FrameworkAgent;
import com.barchart.feed.api.message.Message;

public interface Market extends MarketObject, Comparable<Market>, 
		FrameworkElement<Market> {

	<V extends FrameworkElement<V>> void handle(Message<V> message);
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
