package com.barchart.feed.base.participant;

public interface FrameworkAgentLifecycleHandler {

	void attachAgent(FrameworkAgent<?> agent);

	void updateAgent(FrameworkAgent<?> agent);

	void detachAgent(FrameworkAgent<?> agent);

}
