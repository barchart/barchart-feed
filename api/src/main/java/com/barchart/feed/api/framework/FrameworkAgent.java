package com.barchart.feed.api.framework;


import com.barchart.feed.api.consumer.Agent;
import com.barchart.feed.api.consumer.MarketCallback;
import com.barchart.feed.api.consumer.data.Exchange;
import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.consumer.data.Market;
import com.barchart.feed.api.consumer.data.MarketData;
import com.barchart.feed.api.consumer.enums.MarketEventType;

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
