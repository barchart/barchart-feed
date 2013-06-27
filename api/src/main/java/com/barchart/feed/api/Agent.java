package com.barchart.feed.api;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;

/**
 * 
 */
public interface Agent {

	/* ***** ***** Lifecycle ***** ***** */
	
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

	/* ***** ***** Filter ***** ***** */
	
	/**
	 * Subscribes to and attaches agent to provided symbols.
	 * 
	 * @param symbols
	 */
	void include(CharSequence... symbols);

	/**
	 * Subscribes to and attaches agent to provided instruments.
	 * 
	 * @param instruments
	 */
	void include(Instrument... instruments);

	/**
	 * Subscribes to and attaches agent to all instruments from provided
	 * exchanges. This filter precedes all individual instrument inclusion/
	 * exclusions.
	 * 
	 * @param exchanges
	 */
	void include(Exchange... exchanges);

	/**
	 * Unsubscribes and detaches agent from provided symbols. Performs
	 * instrument lookup.
	 * 
	 * @param symbols
	 */
	void exclude(CharSequence... symbols);

	/**
	 * Unsubscribes and detaches agent from provided instruments
	 * 
	 * @param instruments
	 */
	void exclude(Instrument... instruments);

	/**
	 * Unsubscribes and detaches agent from provided exchanges.
	 * 
	 * @param exchanges
	 */
	void exclude(Exchange... exchanges);

	/**
	 * Unsubscribes agent from all instruments
	 * 
	 * FIXME better name
	 */
	void clear();
}
