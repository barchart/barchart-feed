package com.barchart.feed.api.data;

import java.util.List;

import com.barchart.feed.api.data.common.OrderBookCommon;
import com.barchart.feed.api.data.object.OrderBookObject;
import com.barchart.feed.api.data.primitive.OrderBookPrimitive;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface OrderBook extends OrderBookCommon, OrderBookObject,
		OrderBookPrimitive {

	@Override
	Price bestPrice(MarketSide side);

	@Override
	double bestPriceDouble(MarketSide side);

	@Override
	Size bestSize(MarketSide side);

	@Override
	long bestSizeLong(MarketSide side);

	@Override
	List<PriceLevel> entryList(MarketSide side);

	@Override
	Price lastPrice();

	@Override
	double lastPriceDouble();

	@Override
	Time timeUpdated();

}
