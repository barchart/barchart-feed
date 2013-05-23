package com.barchart.feed.api.framework;

import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.api.data.Market;

public interface MarketEntity extends Market, Comparable<MarketEntity>, 
		FrameworkEntity<MarketEntity> {

	// MARKET TAGS HERE
	
	InstrumentEntity instrumentEntity();
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
