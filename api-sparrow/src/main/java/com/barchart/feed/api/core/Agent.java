package com.barchart.feed.api.core;

import com.barchart.feed.api.data.Exchange;
import com.barchart.feed.api.data.Instrument;

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
	 * Subscribes to and attaches agent to all available instruments.
	 */
	void includeAll();
	
	/**
	 * Subscribes to and attaches agent to all available instruments which
	 * provided acceptor accepts.  This overwrites previous acceptor if method
	 * was previously called.
	 * 
	 * @param acceptor
	 */
	void includeAll(InstrumentAcceptor acceptor);
	
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
