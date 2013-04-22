package com.barchart.feed.api.market;



public interface MarketDataObject<M extends MarketDataObject<M>> {
	
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();

}
