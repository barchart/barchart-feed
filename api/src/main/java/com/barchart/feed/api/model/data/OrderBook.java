package com.barchart.feed.api.model.data;

import java.util.List;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.PriceLevel;

public interface OrderBook extends MarketData<OrderBook> {

	TopOfBook topOfBook();

	List<PriceLevel> entryList(MarketSide side);

	PriceLevel lastBookUpdate();

}
