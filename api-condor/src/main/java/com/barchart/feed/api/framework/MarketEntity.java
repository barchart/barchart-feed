package com.barchart.feed.api.framework;

import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.message.Message;

public interface MarketEntity extends Market, Comparable<MarketEntity>, 
		FrameworkEntity<MarketEntity> {

	InstrumentEntity instrumentEntity();
	
	void handle(Message message);
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
