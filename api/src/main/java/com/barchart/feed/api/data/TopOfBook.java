package com.barchart.feed.api.data;

public interface TopOfBook extends MarketData<TopOfBook> {

	PriceLevel bid();
	
	PriceLevel ask();
	
}
