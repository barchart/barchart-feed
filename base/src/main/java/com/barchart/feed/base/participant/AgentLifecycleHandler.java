package com.barchart.feed.base.participant;

public interface AgentLifecycleHandler {

	void attachAgent(FrameworkAgent<?> agent);

	void updateAgent(FrameworkAgent<?> agent);

	void detachAgent(FrameworkAgent<?> agent);

}
