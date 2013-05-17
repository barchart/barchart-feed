package com.barchart.feed.api.market;

import com.barchart.feed.api.data.FrameworkEntity;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.message.Message;

public interface MarketEntity extends Market, Comparable<MarketEntity>, 
		FrameworkEntity<MarketEntity> {

	void handle(Message message);
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
