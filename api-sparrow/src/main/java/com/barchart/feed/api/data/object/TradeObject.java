package com.barchart.feed.api.data.object;

import com.barchart.feed.api.data.MarketData;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface TradeObject extends MarketData, Tuple {

	@Override
	Price price();

	@Override
	Size size();

	Time time();

}
