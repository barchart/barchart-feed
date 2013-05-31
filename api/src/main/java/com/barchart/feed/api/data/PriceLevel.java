package com.barchart.feed.api.data;

import java.util.List;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel extends MarketData<PriceLevel> {

	Price price();

	double priceDouble();

	Size size();

	long sizeLong();

	MarketSide side();

	int level();

	List<Order> orderList();

}
