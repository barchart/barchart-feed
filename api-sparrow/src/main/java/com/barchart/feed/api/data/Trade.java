package com.barchart.feed.api.data;

import com.barchart.feed.api.data.object.TradeObject;
import com.barchart.feed.api.data.primitive.TradePrimitive;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Trade extends TradeObject, TradePrimitive {

	@Override
	Price price();

	@Override
	double priceDouble();

	@Override
	Size size();

	@Override
	long sizeLong();

	@Override
	Time time();

	@Override
	long timeLong();

}
