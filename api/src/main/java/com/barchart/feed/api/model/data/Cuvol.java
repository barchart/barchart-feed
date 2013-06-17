package com.barchart.feed.api.model.data;

import java.util.List;

import com.barchart.feed.api.model.CuvolEntry;
import com.barchart.feed.api.model.MarketData;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Cuvol extends MarketData<Cuvol> {

	Price firstPrice();
	
	Price tickSize();
	
	// TODO do we want this List<CuvolEntry>?
	List<Size> cuvolList();
	
	CuvolEntry lastCuvolUpdate();
	
}
