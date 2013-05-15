package com.barchart.feed.api.data.object;

import java.util.List;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface CuvolObject {

	Price firstPrice();
	Price tickSize();
	List<Size> cuvolList();
	
}
