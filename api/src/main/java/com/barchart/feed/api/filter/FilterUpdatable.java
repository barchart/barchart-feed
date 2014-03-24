package com.barchart.feed.api.filter;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.id.MetadataID;
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
	@Deprecated
	void include(Metadata... meta);
	
	void include(MetadataID<?>... metaID);
	
	/**
	 * Exclude any previously resolved market entity from the current filter.
	 */
	@Deprecated
	void exclude(Metadata... meta);
	
	void exclude(MetadataID<?>... metaID);
	
}
