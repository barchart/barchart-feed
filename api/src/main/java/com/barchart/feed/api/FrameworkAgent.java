package com.barchart.feed.api;


import com.barchart.feed.api.data.Exchange;
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
	
	Instrument[] instruments();
	Exchange[] exchanges();
	boolean all();
	
}
