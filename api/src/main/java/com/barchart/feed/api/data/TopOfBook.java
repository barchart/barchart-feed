package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.MarketSide;


public interface TopOfBook extends MarketData<TopOfBook> {

	PriceLevel side(MarketSide side);
	
}
