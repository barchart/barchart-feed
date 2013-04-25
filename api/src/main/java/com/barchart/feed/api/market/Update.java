package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.MarketDataObject;


public interface Update<M extends MarketDataObject<M>> 
		extends MarketMessage<M> {

}
