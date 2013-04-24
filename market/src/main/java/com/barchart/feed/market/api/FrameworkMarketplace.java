package com.barchart.feed.market.api;

import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.Marketplace;
import com.barchart.feed.api.market.data.MarketDataObject;


public interface FrameworkMarketplace extends Marketplace {

	void attachAgent(FrameworkAgent agent);
	void updateAgent(FrameworkAgent agent);
	void detachAgent(FrameworkAgent agent);
	
	// dispatch? 
	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
	void attachMarket(Market market);
	void updateMarket(Market market);
	void detachMarket(Market market);
	
}
