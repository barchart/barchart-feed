package com.barchart.feed.api.framework;

import com.barchart.feed.api.consumer.AgentBuilder;
import com.barchart.feed.api.framework.message.Message;

public interface FrameworkMarketplace extends AgentBuilder {

	void attachAgent(FrameworkAgent<?> agent);
	void updateAgent(FrameworkAgent<?> agent);
	void detachAgent(FrameworkAgent<?> agent);

	void handle(Message message);
	
}
