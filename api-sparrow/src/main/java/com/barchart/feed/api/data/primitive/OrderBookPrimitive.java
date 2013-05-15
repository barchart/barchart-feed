package com.barchart.feed.api.data.primitive;

import java.util.List;

import com.barchart.feed.api.data.common.OrderBookCommon;
import com.barchart.feed.api.enums.MarketSide;

public interface OrderBookPrimitive extends OrderBookCommon {

	double bestPriceDouble(MarketSide side);
	
	long bestSizeLong(MarketSide side);
	
	List<Long> sizeListLong(MarketSide side);
	
	double gapDouble();
	
}
