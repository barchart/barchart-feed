package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.PriceLevel;

public interface TopOfBook {

	PriceLevel bid();
	
	PriceLevel ask();
	
}
