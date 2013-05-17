package com.barchart.feed.api.framework;

import com.barchart.feed.api.message.Message;
import com.barchart.missive.api.TagMapSafe;
import com.barchart.util.value.api.Copyable;
import com.barchart.util.value.api.Time;

public interface FrameworkEntity<M extends FrameworkEntity<M>> extends
		TagMapSafe, Copyable<M> {

	Time lastTime();

	Message lastUpdate();

	Message lastSnapshot();

	MarketTag<M> tag();

	void update(Message update);

}
