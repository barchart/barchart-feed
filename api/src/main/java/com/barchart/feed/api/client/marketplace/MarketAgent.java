package com.barchart.feed.api.client.marketplace;

public interface MarketAgent extends MarketCallback {

	public void activate();
	public void deactivate();
	public void dismiss();
	
}
