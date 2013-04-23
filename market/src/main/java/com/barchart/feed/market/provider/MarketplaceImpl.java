package com.barchart.feed.market.provider;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.market.Market;
import com.barchart.feed.api.market.MarketAgent;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.market.api.FrameworkMarketplace;

public class MarketplaceImpl implements FrameworkMarketplace {

	@Override
	public void attachAgent(MarketAgent agent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAgent(MarketAgent agent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detachAgent(MarketAgent agent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attachMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detachMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <V extends MarketDataObject<V>> Builder<V> builder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V extends MarketDataObject<V>> void handle(MarketMessage<V> message) {
		// TODO Auto-generated method stub
		
	}
	
}
