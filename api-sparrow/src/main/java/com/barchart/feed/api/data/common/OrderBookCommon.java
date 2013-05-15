package com.barchart.feed.api.data.common;

import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.enums.MarketSide;

public interface OrderBookCommon {

	PriceLevel[] entries(MarketSide side);
	
}
