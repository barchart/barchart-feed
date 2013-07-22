package com.barchart.feed.api.model;

import java.util.Set;

/**
 * Report market changes in a set.
 */
public interface ChangeSet<T> {

	/**
	 * Report market component changes.
	 */
	Set<T> change();
	
}
