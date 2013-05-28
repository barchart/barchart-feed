package com.barchart.feed.api.framework;

import com.barchart.feed.api.consumer.Agent;
import com.barchart.feed.api.consumer.MarketCallback;
import com.barchart.feed.api.consumer.data.MarketData;
import com.barchart.feed.api.consumer.enums.MarketEventType;

public interface FrameworkAgent<M extends FrameworkEntity<M>, V extends MarketData> 
		extends Agent {
	
	void bindMarketplace(FrameworkMarketplace marketplace);

	MarketTag<M> tag();

	MarketEventType[] eventTypes();
	
	MarketCallback<V> callback();
	
	@Override
	void activate();

	void update();

	@Override
	void deactivate();

	@Override
	void dismiss();

}
