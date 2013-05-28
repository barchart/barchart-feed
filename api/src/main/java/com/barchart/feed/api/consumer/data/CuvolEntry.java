package com.barchart.feed.api.consumer.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface CuvolEntry extends MarketData {
	
	Price price();
	
	Size volume();
	
	int place();

}