package com.barchart.feed.api.consumer.data;

import java.util.List;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Cuvol extends MarketData {

	Price firstPrice();
	
	double firstPriceDouble();
	
	Price tickSize();
	
	double tickSizeDouble();
	
	List<Size> cuvolList();
	
	List<Long> cuvolListLong();
	
}
