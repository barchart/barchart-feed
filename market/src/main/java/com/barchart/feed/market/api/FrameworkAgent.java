package com.barchart.feed.market.api;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.Market;
import com.barchart.feed.api.market.MarketAgent;
import com.barchart.feed.api.market.MarketCallback;
import com.barchart.feed.api.util.Filter;

public interface FrameworkAgent extends Filter<Instrument>, MarketAgent {

//	<V extends MarketDataObject<V>> MarketCallback<V> callback(); 
//	
//	void attach(Market market);
//	void update(Market market);
//	void detach(Market market);
	
}
