package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.filter.Filterable;
import com.barchart.util.value.api.Existential;

/**
 * Top level interface for metadata objects. 
 */
public interface Metadata extends Existential, Filterable {

	enum MetaType {
		AVAILABLE, EXCHANGE, CHANNEL, GROUP, INSTRUMENT, 
	}
	
	String description();
	
	/**
	 * Type of the meta info.
	 */
	MetaType type();
	
	@Override
	boolean isNull();

}
