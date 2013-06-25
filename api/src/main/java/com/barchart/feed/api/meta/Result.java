package com.barchart.feed.api.meta;

/**
 * The result of an asynchronous operation pushed to an observer.  This differs from a
 * future in that a Result is always treated as completed.
 *  
 * @author Gavin M Litchfield
 *
 * @param <V>
 */
public interface Result<V> {

	/**
	 * @return result of an asynchronous operation, can be null;
	 */
	V result();

	/**
	 * 
	 * @return a string representation of the initiating operation
	 */
	String expression();
	
	/**
	 * 
	 * @return reference to an exception if one was thrown during execution,
	 * can be null;
	 */
	Throwable exception();
	
	
}
