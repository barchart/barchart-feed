package com.barchart.feed.meta.service;

/**
 * The result of an asynchronous operation pushed to an observer.  This differs from a
 * future in that a Result is always treated as completed.
 *
 * @author Gavin M Litchfield
 *
 * @param <V>
 */
public interface LookupResult<V> {

	/**
	 * The original query object that triggered this execution.
	 */
	LookupSymbol request();

	/**
	 * The result of an asynchronous operation, can be null. An exception should
	 * be thrown if an error occurred during processing.
	 */
	V result() throws Exception;

}
