package com.barchart.feed.api.data.common;

import java.util.List;

import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.data.Order;
import com.barchart.feed.api.enums.MarketSide;

/**
 * 
 */
public interface PriceLevelCommon extends MarketData {

	/**
	 * Logical index of a price level, starting from 1 for a best price.
	 */
	int level();

	/**
	 * Market side of a price level, BID vs ASK.
	 */
	MarketSide side();

	/**
	 * Order structure of a price level.
	 */
	List<Order> orderList();

}
