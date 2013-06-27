package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.util.Describable;
import com.barchart.feed.api.util.Identifiable;
import com.barchart.feed.api.util.Identifier;
import com.barchart.util.value.api.Existential;

/**
 * Top level interface for metadata objects. 
 */
public interface Metadata extends Existential, Identifiable, Describable {
	
	@Override
	boolean isNull();
	
	@Override
	public Identifier id();
	
	@Override
	public String description();

}
