package com.barchart.feed.api.data;

import java.util.List;

import com.barchart.feed.api.data.common.PriceLevelCommon;
import com.barchart.feed.api.data.object.PriceLevelObject;
import com.barchart.feed.api.data.primitive.PriceLevelPrimitive;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel extends PriceLevelCommon, PriceLevelObject,
		PriceLevelPrimitive {

	@Override
	Price price();

	@Override
	double priceDouble();

	@Override
	Size size();

	@Override
	long sizeLong();

	@Override
	MarketSide side();

	@Override
	int level();

	@Override
	List<Order> orderList();

}
