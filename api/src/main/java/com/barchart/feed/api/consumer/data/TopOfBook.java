package com.barchart.feed.api.consumer.data;

import com.barchart.feed.api.consumer.enums.MarketSide;


public interface TopOfBook extends MarketData {

	PriceLevel side(MarketSide side);
	
}
