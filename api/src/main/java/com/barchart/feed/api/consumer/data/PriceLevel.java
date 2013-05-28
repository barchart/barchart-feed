package com.barchart.feed.api.consumer.data;

import java.util.List;

import com.barchart.feed.api.consumer.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel extends MarketData {

	Price price();

	double priceDouble();

	Size size();

	long sizeLong();

	MarketSide side();

	int level();

	List<Order> orderList();

}