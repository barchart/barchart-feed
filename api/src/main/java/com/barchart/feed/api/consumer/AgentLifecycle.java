package com.barchart.feed.api.consumer;


public interface AgentLifecycle {

	/**
	 * Agent life cycle state.
	 */
	enum State {

		/** Agent is being created. */
		CREATED, //

		/** Agent is processing events. */
		ACTIVATED, //

		/** Agent event processing is suspended. */
		DEACTIVATED, //

		/** Agent is terminated and can not be used again. */
		TERMINATED, //

	}
	
	/**
	 * Current agent life cycle state.
	 */
	State state();
	
	/**
	 * Agent will fire events while active.
	 */
	boolean isActive();

	/**
	 * The default state of an agent, firing callback once the first instrument
	 * is included.
	 */
	void activate();

	/**
	 * When deactivated, agent will remain attached to all requested
	 * instruments, but not fire callback.
	 */
	void deactivate();

	/**
	 * Permanently removes all references to the agent from the framework.
	 * Activating a dismissed agent will have no effect.
	 */
	void terminate();
	
}
