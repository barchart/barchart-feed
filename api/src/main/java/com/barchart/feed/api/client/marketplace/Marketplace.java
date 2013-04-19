package com.barchart.feed.api.client.marketplace;

public interface Marketplace {
	
	void attachAgent(MarketAgent agent);
	void updateAgent(MarketAgent agent);
	void detachAgent(MarketAgent agent);

}
