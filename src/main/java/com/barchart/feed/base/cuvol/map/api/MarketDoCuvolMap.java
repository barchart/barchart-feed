package com.barchart.feed.base.cuvol.map.api;

import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

public interface MarketDoCuvolMap extends MarketCuvolMap {

	void add(PriceValue price, SizeValue size);

	MarketCuvolEntry getLastEntry();

	void clear();
	
}
