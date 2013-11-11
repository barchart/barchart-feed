package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.filter.Filterable;
import com.barchart.feed.api.util.Describable;
import com.barchart.util.value.api.Existential;

/**
 * Top level interface for metadata objects. 
 */
public interface Metadata extends Existential, Describable, Filterable {

	@Override
	boolean isNull();
	
	@Override
	String description();

	@Override
	MetaType type();
}
