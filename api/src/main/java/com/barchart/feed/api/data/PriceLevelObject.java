package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.MarketSide;


public interface PriceLevelObject extends MarketDataObject<PriceLevelObject> {

	double price();
	
	long size();
	
	MarketSide side();
	
	int place();
	
}
