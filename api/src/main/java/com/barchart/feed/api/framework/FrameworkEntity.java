package com.barchart.feed.api.framework;

import com.barchart.feed.api.consumer.data.MarketData;
import com.barchart.feed.api.framework.message.Message;
import com.barchart.missive.api.TagMapSafe;
import com.barchart.util.value.api.Time;

public interface FrameworkEntity<M extends FrameworkEntity<M>> extends
		TagMapSafe {

//  Not sure how messages will be exposed, if at all
//	Message lastUpdate();
//	Message lastSnapshot();

	MarketTag<M> tag();
	MarketData data();
	Time lastTime();

	void update(Message update);

}
