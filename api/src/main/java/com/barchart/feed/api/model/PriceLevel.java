package com.barchart.feed.api.model;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel {

	Price price();

	Size size();

	MarketSide side();

	int level();

}
