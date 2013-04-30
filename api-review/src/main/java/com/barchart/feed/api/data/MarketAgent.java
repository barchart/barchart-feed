package com.barchart.feed.api.data;

import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.util.Filter;

public interface MarketAgent<V extends MarketDataObject> 
		extends Filter<Instrument>, MarketCallback<V> {

	public void activate();
	public void update();
	public void deactivate();
	public void dismiss();
	
}
