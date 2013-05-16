package com.barchart.feed.api.data.primitive;

import com.barchart.feed.api.data.common.PriceLevelCommon;

/**
 * Price level as primitives.
 */
public interface PriceLevelPrimitive extends PriceLevelCommon {

	@Override
	int level();

	/**
	 * Price of a price level, as double.
	 */
	double priceDouble();

	/**
	 * Total order quantity of a price level, as long.
	 */
	long sizeLong();

}
