package com.barchart.feed.api.data.client.object;

import java.util.List;

import com.barchart.feed.api.data.client.common.OrderBookCommon;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface OrderBookObject extends OrderBookCommon {
	
	Price bestPrice(MarketSide side);
	
	Size bestSize(MarketSide side);
	
	List<Size> sizeList(MarketSide side);
	
	Price gap();

}
