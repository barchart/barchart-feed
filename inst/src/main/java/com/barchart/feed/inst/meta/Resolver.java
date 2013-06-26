package com.barchart.feed.inst.meta;

import java.util.List;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.util.Observer;

/**
 * 
 * Search
 *
 * @param <V>
 */
public interface Resolver<V extends Metadata> {
	
	/**
	 * Returns a builder which executes an observer with 
	 * the results of a search.
	 *
	 * @param <V>
	 */
	interface Builder<V extends Metadata> {
		
		// Other options here
		
		Resolver<V> build(Observer<Result<List<V>>> observer);
		
	}

	/**
	 * Resolve a search expression.
	 * <p>
	 * Return a list ordered per current context "best match" rules.
	 * 
	 * @param expression 
	 * 				the expression to search for
	 * @param offset
	 * 				result paging: index of first entry to return
	 * @param limit
	 *  			result paging: limit number of returned entries
	 */
	void resolve(String expression, int offset, int limit, long timeout);
	
}
