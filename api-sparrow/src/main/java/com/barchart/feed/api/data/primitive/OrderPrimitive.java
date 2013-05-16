package com.barchart.feed.api.data.primitive;

import com.barchart.feed.api.data.common.OrderCommon;

public interface OrderPrimitive extends OrderCommon {

	@Override
	String id();

	double priceDouble();

	long sizeDouble();

}
