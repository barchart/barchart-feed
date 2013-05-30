package com.barchart.feed.api.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/**
 * Single order in a book price level.
 */
public interface Order extends MarketData {

	String id();

	Price price();

	double priceDouble();

	Size size();

	long sizeDouble();

}
