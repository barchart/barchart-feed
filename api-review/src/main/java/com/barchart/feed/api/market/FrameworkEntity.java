package com.barchart.feed.api.market;

import com.barchart.feed.api.data.Time;
import com.barchart.feed.api.message.Message;
import com.barchart.feed.api.util.Copyable;
import com.barchart.missive.api.TagMapSafe;

public interface FrameworkEntity<M extends FrameworkEntity<M>> extends TagMapSafe, 
		Copyable<M> {
	
	Time lastTime();
	Message lastUpdate();
	Message lastSnapshot();
	
	MarketTag<M> tag();
	void update(Message update);

}
