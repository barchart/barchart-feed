package com.barchart.feed.api.market;

import com.barchart.feed.api.data.MarketDataObject;

public interface MarketAgent {

	public void activate();
	public void update();
	public void deactivate();
	public void dismiss();
	
	<V extends MarketDataObject<V>> MarketCallback<V> callback(); 
	
	void attach(Market market);
	void update(Market market);
	void detach(Market market);
	
}
