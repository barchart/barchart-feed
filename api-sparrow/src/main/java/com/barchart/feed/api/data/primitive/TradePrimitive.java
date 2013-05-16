package com.barchart.feed.api.data.primitive;

import com.barchart.feed.api.data.MarketData;

public interface TradePrimitive extends MarketData {

	double priceDouble();

	long sizeLong();

	long timeLong();

}
