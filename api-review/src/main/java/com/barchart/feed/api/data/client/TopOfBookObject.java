package com.barchart.feed.api.data.client;

import com.barchart.feed.api.enums.MarketSide;


public interface TopOfBookObject extends MarketDataObject {

	PriceLevelObject side(MarketSide side);
	
}
