package com.barchart.feed.api.filter;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

/**
 * Instance with an updatable filter.
 */
@ProviderType
public interface FilterUpdatable {

	/**
	 * Current filter.
	 */
	Filter filter();

	/**
	 * Replace old with new filter.
	 */
	void filter(Filter filter);

	/**
	 * Remove all filter definitions.
	 */
	void clear();

	/**
	 * Include any previously resolved market entity into the current filter.
	 */
	void include(Metadata... meta);
	
	/**
	 * Exclude any previously resolved market entity from the current filter.
	 */
	void exclude(Metadata... meta);
	
}
