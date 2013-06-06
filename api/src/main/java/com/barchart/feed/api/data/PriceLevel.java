package com.barchart.feed.api.data;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface PriceLevel extends MarketData<PriceLevel> {

	Price price();

	Size size();

	MarketSide side();

	int level();

}
