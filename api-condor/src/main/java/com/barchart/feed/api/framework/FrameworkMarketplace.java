package com.barchart.feed.api.framework;

import com.barchart.feed.api.core.Marketplace;
import com.barchart.feed.api.message.Message;

public interface FrameworkMarketplace extends Marketplace {

	
	void attachAgent(FrameworkAgent agent);

	void updateAgent(FrameworkAgent agent);

	void detachAgent(FrameworkAgent agent);

	void handle(Message message);
	
}
