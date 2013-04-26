package com.barchart.feed.api.message;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.missive.api.TagMap;

public interface Message<M extends FrameworkElement<M>> extends TagMap {

	Instrument instrument();
	MarketTag<M> tag();
	DateTime time();
	
}
