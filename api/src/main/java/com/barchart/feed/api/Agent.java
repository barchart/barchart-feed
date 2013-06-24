package com.barchart.feed.api;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;

/**
 * Filter hierarchy:
 * 
 *  All Filter
 *  Exchange Filter
 *  Instrument Filter
 *  Custom filter
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Agent {

	boolean isActive();
	
	/**
	 * The default state of an agent, firing callback once the first
	 * instrument is included.
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
	void dismiss();
	
	/**
	 * Subscribes to and attaches agent to provided symbols.
	 * 
	 * @param symbols
	 */
	void include(CharSequence...symbols);
	
	/**
	 * Subscribes to and attaches agent to provided instruments.
	 * 
	 * @param instruments
	 */
	void include(Instrument...instruments);
	
	
	/**
	 * Subscribes to and attaches agent to all instruments from provided
	 * exchanges.  This filter precedes all individual instrument inclusion/
	 * exclusions.
	 * 
	 * @param exchanges
	 */
	void include(Exchange...exchanges);
	
	/**
	 * Unsubscribes and detaches agent from provided symbols.  Performs
	 * instrument lookup.
	 * 
	 * @param symbols
	 */
	void exclude(CharSequence...symbols);
	
	/**
	 * Unsubscribes and detaches agent from provided instruments
	 * @param instruments
	 */
	void exclude(Instrument...instruments);
	
	/**
	 * Unsubscribes and detaches agent from provided exchanges.
	 * 
	 * @param exchanges
	 */
	void exclude(Exchange...exchanges);
	
	/**
	 * Unsubscribes agent from all instruments
	 */
	void clear();
}
