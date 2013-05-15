package com.barchart.feed.api.data.client;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel extends MarketData {

	Price price();
	double priceDouble();
	
	Size quantity();
	long quantityLong();
	
	MarketSide side();
	
	int place();
	
	BookLiquidityType type();
	
}
