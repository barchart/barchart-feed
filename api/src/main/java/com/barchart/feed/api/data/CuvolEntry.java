package com.barchart.feed.api.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface CuvolEntry extends MarketData<CuvolEntry> {
	
	Price price();
	
	Size volume();
	
	int place();

}
