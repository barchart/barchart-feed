package com.barchart.feed.api.framework.market;

import com.barchart.feed.api.commons.api.Filter;
import com.barchart.feed.api.commons.api.Market;
import com.barchart.feed.api.commons.api.MarketCallback;
import com.barchart.feed.api.commons.api.MarketDataObject;
import com.barchart.feed.api.framework.inst.Instrument;

public interface FrameworkAgent extends Filter<Instrument> {

	<V extends MarketDataObject<V>> MarketCallback<V> callback(); 
	
	void attach(Market market);
	void update(Market market);
	void detach(Market market);
	
}
