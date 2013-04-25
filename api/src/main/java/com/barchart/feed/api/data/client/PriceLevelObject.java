package com.barchart.feed.api.data.client;

import com.barchart.feed.api.enums.MarketSide;


public interface PriceLevelObject extends MarketDataObject {

	double price();
	
	long size();
	
	MarketSide side();
	
	int place();
	
}
