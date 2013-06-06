package com.barchart.feed.api.data;

import java.util.List;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Cuvol extends MarketData<Cuvol> {

	Price firstPrice();
	
	Price tickSize();
	
	List<Size> cuvolList();
	
}
