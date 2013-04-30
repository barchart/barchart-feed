package com.barchart.feed.api.data;

import org.joda.time.DateTime;

import com.barchart.feed.api.message.Message;
import com.barchart.missive.api.TagMapSafe;

public interface FrameworkElement<M extends FrameworkElement<M>> extends TagMapSafe {
	
	DateTime lastTime();
	Message lastUpdate();
	Message lastSnapshot();
	
	MarketTag<M> tag();
	void update(Message update);

}
