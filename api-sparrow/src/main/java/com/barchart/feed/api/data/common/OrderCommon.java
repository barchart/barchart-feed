package com.barchart.feed.api.data.common;

import com.barchart.feed.api.data.MarketData;

public interface OrderCommon extends MarketData {

	/**
	 * Unique order id in a given scope.
	 */
	String id();

}
