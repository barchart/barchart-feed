package com.barchart.feed.api.data.primitive;

import com.barchart.feed.api.data.common.OrderBookCommon;
import com.barchart.feed.api.enums.MarketSide;

public interface OrderBookPrimitive extends OrderBookCommon {

	double bestPriceDouble(MarketSide side);

	long bestSizeLong(MarketSide side);

	double lastPriceDouble();

	long timeUpdatedLong();

}
