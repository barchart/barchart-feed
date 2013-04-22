package com.barchart.feed.api.data;

import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;



public interface MarketDataObject<M extends MarketDataObject<M>> {
	
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();

}
