package com.barchart.feed.api.data.primitive;

import com.barchart.feed.api.data.common.OrderCommon;

public interface OrderPrimitive extends OrderCommon {

	@Override
	String id();

	/**
	 * Price of an order, as double.
	 */
	double priceDouble();

	/**
	 * Size of an order, as long.
	 */
	long sizeDouble();

}
