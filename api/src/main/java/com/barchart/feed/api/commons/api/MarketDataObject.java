package com.barchart.feed.api.commons.api;


public interface MarketDataObject<M extends MarketDataObject<M>> {
	
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();

}
