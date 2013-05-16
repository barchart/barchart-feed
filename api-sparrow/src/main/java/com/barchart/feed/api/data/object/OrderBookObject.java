package com.barchart.feed.api.data.object;

import com.barchart.feed.api.data.common.OrderBookCommon;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface OrderBookObject extends OrderBookCommon {

	Price bestPrice(MarketSide side);

	Size bestSize(MarketSide side);

	Price lastPrice();

	Time timeUpdated();

}
