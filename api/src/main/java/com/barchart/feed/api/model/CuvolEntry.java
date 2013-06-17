package com.barchart.feed.api.model;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface CuvolEntry {
	
	Price price();
	
	Size volume();
	
	int place();

}
