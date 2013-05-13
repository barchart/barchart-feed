package com.barchart.feed.api.data.client;

import com.barchart.feed.api.enums.MarketSide;


public interface TopOfBook extends MarketData {

	PriceLevelObject side(MarketSide side);
	
}
