package com.barchart.feed.api.data.client;


public interface CuvolObject extends MarketDataObject {

	double firstPrice();
	double tickSize();
	long[] cuvols();
	
}
