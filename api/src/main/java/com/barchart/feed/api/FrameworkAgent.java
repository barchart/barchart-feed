package com.barchart.feed.api;

import java.util.Set;

import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;

public interface FrameworkAgent<V extends MarketData<V>> extends Agent {
	
	Class<V> type();
	MarketCallback<V> callback();
	V data(Market market);
	
	/* Filter methods */
	boolean accept(Instrument instrument);
	
	Set<String> interests();
	
}
