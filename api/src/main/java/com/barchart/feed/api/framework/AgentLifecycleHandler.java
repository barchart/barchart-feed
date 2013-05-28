package com.barchart.feed.api.framework;

public interface AgentLifecycleHandler {

	void attachAgent(FrameworkAgent<?> agent);
	void updateAgent(FrameworkAgent<?> agent);
	void detachAgent(FrameworkAgent<?> agent);
	
}
