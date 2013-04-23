package com.barchart.feed.api.data;


public interface CuvolObject extends MarketDataObject<CuvolObject> {

	double firstPrice();
	double tickSize();
	long[] cuvols();
	
}
