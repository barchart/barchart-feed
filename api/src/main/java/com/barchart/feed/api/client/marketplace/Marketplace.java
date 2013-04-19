package com.barchart.feed.api.client.marketplace;

import com.barchart.feed.api.commons.api.Market;

public interface Marketplace {
	
	void attachAgent(MarketAgent agent);
	void updateAgent(MarketAgent agent);
	void detachAgent(MarketAgent agent);

	void attachMarket(Market market);
	void updateMarket(Market market);
	void detachMarket(Market market);
	
}
