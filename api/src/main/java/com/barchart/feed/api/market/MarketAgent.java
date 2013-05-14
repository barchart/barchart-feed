package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.util.Inclusive;

public interface MarketAgent<V extends MarketDataObject> 
		extends Inclusive<Instrument>, MarketCallback<V> {

	public void activate();
	public void update();
	public void deactivate();
	public void dismiss();
	
}
