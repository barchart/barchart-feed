package com.barchart.feed.api;

/** FIXME shift down */
public interface AgentLifecycleHandler {

	void attachAgent(FrameworkAgent<?> agent);

	void updateAgent(FrameworkAgent<?> agent);

	void detachAgent(FrameworkAgent<?> agent);

}
