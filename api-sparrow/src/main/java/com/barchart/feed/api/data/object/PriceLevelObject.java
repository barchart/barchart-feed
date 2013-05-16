package com.barchart.feed.api.data.object;

import com.barchart.feed.api.data.common.PriceLevelCommon;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Tuple;

/**
 * Price level as values.
 */
public interface PriceLevelObject extends PriceLevelCommon, Tuple {

	/**
	 * Price of a price level, as {@link Price} value.
	 */
	@Override
	Price price();

	/**
	 * Total order quantity of a price level, as {@link Size} value.
	 */
	@Override
	Size size();

	@Override
	MarketSide side();

}
