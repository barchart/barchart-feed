package com.barchart.feed.api.data.client.common;

import com.barchart.feed.api.data.client.PriceLevel;
import com.barchart.feed.api.enums.MarketSide;

public interface OrderBookCommon {

	PriceLevel[] entries(MarketSide side);
	
}
