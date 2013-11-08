package com.barchart.feed.api.util;

public interface Identifiable {

	/**
	 * Unique identifier in a given scope.
	 */
	Identifier id();
	
	
	@Override
	int hashCode();
	
	/**
	 * 
	 * @param o
	 * @return True if object is the same or has an Identifier equal to
	 * the identifier of this object.
	 */
	@Override
	boolean equals(Object o);
	
}
