package com.barchart.feed.api;


public interface AgentLifecycleHandler {

	void attachAgent(FrameworkAgent<?> agent);
	void updateAgent(FrameworkAgent<?> agent);
	void detachAgent(FrameworkAgent<?> agent);
	
}
