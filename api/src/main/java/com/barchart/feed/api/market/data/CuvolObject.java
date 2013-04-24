package com.barchart.feed.api.market.data;


public interface CuvolObject extends MarketDataObject<CuvolObject> {

	double firstPrice();
	double tickSize();
	long[] cuvols();
	
}
