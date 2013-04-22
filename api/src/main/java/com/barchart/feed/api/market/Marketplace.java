package com.barchart.feed.api.market;

public interface Marketplace {
	
	void attachAgent(MarketAgent agent);
	void updateAgent(MarketAgent agent);
	void detachAgent(MarketAgent agent);

}
