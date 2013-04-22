package com.barchart.feed.api.market;

import com.barchart.feed.api.inst.Instrument;

public interface FrameworkAgent extends Filter<Instrument>, MarketAgent {

	<V extends MarketDataObject<V>> MarketCallback<V> callback(); 
	
	void attach(Market market);
	void update(Market market);
	void detach(Market market);
	
}
