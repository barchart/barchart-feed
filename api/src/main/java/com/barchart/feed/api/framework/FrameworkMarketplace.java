package com.barchart.feed.api.framework;

import com.barchart.feed.api.consumer.Marketplace;
import com.barchart.feed.api.framework.message.Message;

public interface FrameworkMarketplace extends Marketplace {

	
	void attachAgent(FrameworkAgent agent);

	void updateAgent(FrameworkAgent agent);

	void detachAgent(FrameworkAgent agent);

	void handle(Message message);
	
}
