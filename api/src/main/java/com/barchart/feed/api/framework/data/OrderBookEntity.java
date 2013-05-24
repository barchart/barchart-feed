package com.barchart.feed.api.framework.data;

import com.barchart.feed.api.consumer.data.OrderBook;
import com.barchart.feed.api.framework.FrameworkEntity;


public interface OrderBookEntity extends OrderBook, FrameworkEntity<OrderBookEntity> {

	void setEntry(PriceLevelEntity entry);
	// UniBookResult setSnapshot(PriceLevel[]);
	
	// void clear()
	
}
