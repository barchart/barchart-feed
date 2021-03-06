package com.barchart.feed.api;

import rx.Observable;

import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MetadataService.Result;
import com.barchart.feed.api.filter.Filter;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.id.MetadataID;

/**
 * General agent for users, adds include/exclude w/o Rx.  Should not be used by platform. 
 */
public interface Agent extends ConsumerAgent {

	/**
	 * Subscribes to and attaches agent to provided symbols.
	 * 
	 * @param symbols
	 */
	void includeSymbol(String... symbols);
	
	/**
	 * Unsubscribes and detaches agent from provided symbols. Performs
	 * instrument lookup.
	 * 
	 * @param symbols
	 */
	void excludeSymbol(String... symbols);
	
	
	/* ***** ***** ConsumerAgent ***** ***** */
	
	/**
	 * Subscribes to and attaches agent to provided symbols.
	 * 
	 * @param symbols
	 */
	@Override
	Observable<Result<Instrument>> include(String... symbols);
	
	/**
	 * Unsubscribes and detaches agent from provided symbols. Performs
	 * instrument lookup.
	 * 
	 * @param symbols
	 */
	@Override
	Observable<Result<Instrument>> exclude(String... symbols);
	
	/* ***** ***** Lifecycle ***** ***** */
	
	/**
	 * Current agent life cycle state.
	 */
	@Override
	State state();
	
	/**
	 * Agent will fire events while active.
	 */
	@Override
	boolean isActive();

	/**
	 * The default state of an agent, firing callback once the first instrument
	 * is included.
	 */
	@Override
	void activate();

	/**
	 * When deactivated, agent will remain attached to all requested
	 * instruments, but not fire callback.
	 */
	@Override
	void deactivate();

	/**
	 * Permanently removes all references to the agent from the framework.
	 * Activating a dismissed agent will have no effect.
	 */
	@Override
	void terminate();
	
	/* ***** ***** Filter ***** ***** */
	
	/**
	 * Verify that market matches the filter.
	 */
	@Override
	boolean hasMatch(Instrument instrument);

	/**
	 * Resolved filter in the LDAP format http://www.ietf.org/rfc/rfc1960.txt.
	 */
	@Override
	String expression();
	
	/* ***** ***** Filter Updatable ***** ***** */

	/**
	 * Current filter.
	 */
	@Override
	Filter filter();

	/**
	 * Replace old with new filter.
	 */
	@Override
	void filter(Filter filter);
	
	/**
	 * Subscribes to and attaches agent to provided instruments.
	 * 
	 * @param instruments
	 */
	@Override
	@Deprecated
	void include(Metadata... meta);
	
	@Override
	void include(MetadataID<?>... metaID);

	/**
	 * Unsubscribes and detaches agent from provided instruments
	 * 
	 * @param instruments
	 */
	@Override
	@Deprecated
	void exclude(Metadata... meta);
	
	@Override
	void exclude(MetadataID<?>... metaID);

	/**
	 * Unsubscribes agent from all instruments
	 */
	@Override
	void clear();
	
}
