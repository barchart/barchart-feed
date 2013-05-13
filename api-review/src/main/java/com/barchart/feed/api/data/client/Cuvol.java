package com.barchart.feed.api.data.client;

import java.util.List;


public interface Cuvol extends MarketData {

	double firstPrice();
	double tickSize();
	List<Long> cuvolList();
	
}
