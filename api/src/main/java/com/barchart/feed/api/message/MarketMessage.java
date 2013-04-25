package com.barchart.feed.api.message;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.MarketTag;

public interface MarketMessage<M extends FrameworkElement<M>> {

	MarketTag<M> tag();
	
}
