package com.barchart.feed.api.message;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.MarketTag;
import com.barchart.missive.api.TagMap;

public interface Message<M extends FrameworkElement<M>> extends TagMap {

	MarketTag<M> tag();
	
}
