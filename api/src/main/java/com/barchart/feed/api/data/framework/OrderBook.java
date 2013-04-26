package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.client.OrderBookObject;

public interface OrderBook extends OrderBookObject, FrameworkElement<OrderBook> {

	// UniBookResult setEntry(PriceLevel);
	// UniBookResult setSnapshot(PriceLevel);
	
	// void clear()
	
	// PriceLevel last();
	// PriceLevel top(MarketSide side);
	
}
