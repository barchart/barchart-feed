package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.MarketSide;


public interface TopOfBook extends MarketData {

	PriceLevel side(MarketSide side);
	
}
