package com.barchart.feed.api;

import java.util.Set;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;

public interface FrameworkAgent<V extends MarketData<V>> extends Agent {
	
	Class<V> type();
	MarketObserver<V> callback();
	V data(Market market);
	
	/* Filter methods */
	boolean accept(Instrument instrument);
	
	Set<String> interests();
	
}
