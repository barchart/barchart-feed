package com.barchart.feed.api.data.client;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.MarketSide;


public interface PriceLevelObject extends MarketData {

	double priceLevel();
	
	long quantityAtPrice();
	
	MarketSide side();
	
	int place();
	
	BookLiquidityType type();
	
}
