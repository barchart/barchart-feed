package com.barchart.feed.api.market;

import com.barchart.feed.api.message.Message;

public interface FrameworkMarketplace {

	
	void attachAgent(FrameworkAgent agent);

	void updateAgent(FrameworkAgent agent);

	void detachAgent(FrameworkAgent agent);

	void handle(Message message);
	
}
