package com.barchart.feed.api.data.common;

import java.util.List;

import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.enums.MarketSide;

public interface OrderBookCommon {

	List<PriceLevel> entryList(MarketSide side);

}
