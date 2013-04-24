package com.barchart.feed.api.market;

import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.missive.api.Tag;

public interface MarketMessage<M extends MarketDataObject<M>> {

	Tag<M> tag();
	
}
