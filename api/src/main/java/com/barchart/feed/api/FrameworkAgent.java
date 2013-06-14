package com.barchart.feed.api;

import java.util.Set;

import com.barchart.feed.api.connection.Subscription;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.enums.MarketEventType;

public interface FrameworkAgent<V extends MarketData> extends Agent {
	
	MarketEventType[] eventTypes();
	MarketCallback<V> callback();
	V data(Market market);
	
	/* Filter methods */
	boolean accept(Instrument instrument);
	
	Set<String> interests();
	
}
