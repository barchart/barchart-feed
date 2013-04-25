package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;

public interface MarketDataObject<M extends MarketDataObject<M>> {

	MarketTag<?> tag();
	
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();
	
	// These will be moved to a different interface
	void update(Update<M> update);
	void snapshot(Snapshot<M> snapshot);
	
	// lastUpdateTime?
	// lastSnapshotTime?

}
