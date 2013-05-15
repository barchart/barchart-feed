package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.data.client.OrderBook;
import com.barchart.feed.api.market.FrameworkEntity;

public interface OrderBookEntity extends OrderBook, FrameworkEntity<OrderBookEntity> {

	void setEntry(PriceLevelEntity entry);
	// UniBookResult setSnapshot(PriceLevel[]);
	
	// void clear()
	
}
