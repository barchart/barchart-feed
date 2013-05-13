package com.barchart.feed.api.data.framework;

import com.barchart.feed.api.data.client.Market;
import com.barchart.feed.api.market.FrameworkAgent;
import com.barchart.feed.api.market.FrameworkEntity;
import com.barchart.feed.api.message.Message;

public interface MarketEntity extends Market, Comparable<MarketEntity>, 
		FrameworkEntity<MarketEntity> {

	void handle(Message message);
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
}
