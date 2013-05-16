package com.barchart.feed.api.data;

import java.util.List;

import com.barchart.feed.api.data.object.OrderBookObject;
import com.barchart.feed.api.data.primitive.OrderBookPrimitive;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface OrderBook extends MarketData, OrderBookObject,
		OrderBookPrimitive {

	@Override
	PriceLevel[] entries(MarketSide side);

	@Override
	Price bestPrice(MarketSide side);

	@Override
	double bestPriceDouble(MarketSide side);

	@Override
	Size bestSize(MarketSide side);

	@Override
	long bestSizeLong(MarketSide side);

	@Override
	List<Size> sizeList(MarketSide side);

	@Override
	List<Long> sizeListLong(MarketSide side);

}
