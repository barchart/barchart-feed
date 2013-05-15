package com.barchart.feed.api.data;


public interface OrderBookEntity extends OrderBook, FrameworkEntity<OrderBookEntity> {

	void setEntry(PriceLevelEntity entry);
	// UniBookResult setSnapshot(PriceLevel[]);
	
	// void clear()
	
}
