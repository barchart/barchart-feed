package com.barchart.feed.api.data;

import com.barchart.feed.api.message.Snapshot;
import com.barchart.feed.api.message.Update;

public interface FrameworkElement<M extends FrameworkElement<M>> {
	
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();
	
	MarketTag<M> tag();
	
	void update(Update<M> update);
	void snapshot(Snapshot<M> snapshot);

}
