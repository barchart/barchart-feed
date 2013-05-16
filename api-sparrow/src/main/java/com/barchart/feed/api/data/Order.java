package com.barchart.feed.api.data;

import com.barchart.feed.api.data.object.OrderObject;
import com.barchart.feed.api.data.primitive.OrderPrimitive;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Order extends OrderObject, OrderPrimitive {

	@Override
	String id();

	@Override
	Price price();

	@Override
	double priceDouble();

	@Override
	Size size();

	@Override
	long sizeDouble();

}
