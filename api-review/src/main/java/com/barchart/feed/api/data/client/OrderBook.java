package com.barchart.feed.api.data.client;

import com.barchart.feed.api.enums.MarketSide;

public interface OrderBook extends MarketData {

	PriceLevelObject[] entries(MarketSide side);
	
	double bestPrice(MarketSide side);
	
	long bestSize(MarketSide side);
	
	long[] sizes(MarketSide side);
	
	double gap();
	
}
