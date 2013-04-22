package com.barchart.feed.api.market;

import com.barchart.feed.api.data.MarketDataObject;


public interface Snapshot<M extends MarketDataObject<M>> 
		extends MarketMessage<M> {

}
