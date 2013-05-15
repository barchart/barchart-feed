package com.barchart.feed.api.data.client;

import java.util.List;

import com.barchart.feed.api.data.client.object.CuvolObject;
import com.barchart.feed.api.data.client.primitive.CuvolPrimitive;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Cuvol extends MarketData, CuvolPrimitive, CuvolObject {

	@Override
	Price firstPrice();
	
	@Override
	double firstPriceDouble();
	
	@Override
	Price tickSize();
	
	@Override
	double tickSizeDouble();
	
	@Override
	List<Size> cuvolList();
	
	@Override
	List<Long> cuvolListLong();
	
}
