package com.barchart.feed.api.market;


public interface FrameworkMarketplace extends Marketplace {

	// dispatch? 
	void handle(MarketMessage<?> message);
	
	void attachMarket(Market market);
	void updateMarket(Market market);
	void detachMarket(Market market);
	
}
