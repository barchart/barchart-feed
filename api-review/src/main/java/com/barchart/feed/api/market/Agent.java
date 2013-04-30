package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.MarketDataObject;

public interface Agent<V extends MarketDataObject> 
		extends InstrumentFilter, MarketCallback<V> {

	public void activate();
	public void update();
	public void deactivate();
	public void dismiss();
	
}
