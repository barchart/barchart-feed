package com.barchart.feed.market.provider;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.Marketplace;


public interface FrameworkMarketplace extends Marketplace {

	// dispatch? 
	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
	void attachMarket(Market market);
	void updateMarket(Market market);
	void detachMarket(Market market);
	
}
