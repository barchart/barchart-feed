package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.book.OrderBookAction;
import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.client.PriceLevelObject;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

// Maybe this isn't a framework element
public interface PriceLevel extends PriceLevelObject, FrameworkElement<PriceLevel> {

	OrderBookAction act();
	
	PriceValue price();
	
	SizeValue quantity();
	
}
