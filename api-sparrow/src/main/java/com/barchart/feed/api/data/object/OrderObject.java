package com.barchart.feed.api.data.object;

import com.barchart.feed.api.data.common.OrderCommon;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Tuple;

public interface OrderObject extends OrderCommon, Tuple {

	@Override
	String id();

	/**
	 * Price of an order, as {@link Price} value.
	 */
	@Override
	Price price();

	/**
	 * Size of an order, as {@link Size} value.
	 */
	@Override
	Size size();

}
