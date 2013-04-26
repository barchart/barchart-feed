package com.barchart.feed.api.data;

import org.joda.time.DateTime;

import com.barchart.feed.api.message.Snapshot;
import com.barchart.feed.api.message.Update;
import com.barchart.missive.api.TagMapSafe;

public interface FrameworkElement<M extends FrameworkElement<M>> extends TagMapSafe {
	
	DateTime lastTime();
	Update<M> lastUpdate();
	Snapshot<M> lastSnapshot();
	
	MarketTag<M> tag();
	
	void update(Update<M> update);
	void snapshot(Snapshot<M> snapshot);

}
