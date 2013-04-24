package com.barchart.feed.api.market.data;

import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;
import com.barchart.missive.api.Tag;



public interface MarketDataObject<M extends MarketDataObject<M>> {

	Tag<M> tag();
	
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();
	
	// These will be moved to a different interface
	void update(Update<M> update);
	void snapshot(Snapshot<M> snapshot);
	
	// lastUpdateTime?
	// lastSnapshotTime?

}
