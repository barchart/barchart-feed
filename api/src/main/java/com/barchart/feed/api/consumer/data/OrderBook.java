package com.barchart.feed.api.consumer.data;

import java.util.List;

import com.barchart.feed.api.consumer.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface OrderBook extends MarketData {

	Price bestPrice(MarketSide side);

	double bestPriceDouble(MarketSide side);

	Size bestSize(MarketSide side);

	long bestSizeLong(MarketSide side);

	List<PriceLevel> entryList(MarketSide side);

	Price lastPrice();

	double lastPriceDouble();

	Time timeUpdated();

}
