package com.barchart.feed.inst.meta;

import java.util.Collection;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.util.observer.Observer;

/**
 * Service which executes exact match metadata lookups on an observer.
 *
 * @param <V>
 */
public interface Retriever<V extends Metadata> {

	interface Builder<V extends Metadata> {
		
		// Other options here
		
		Retriever<V> build(Observer<Result<V>> observer);
		
	}
	
	/**
	 * Attempts to retrieve a Metadata object from some service and returns
	 * a result to the user's observer.  
	 * <p>
	 * Implementation default timeout used.
	 * 
	 * @param id the GUID of a MetaData object
	 */
	void retrieve(String id);
	
	/**
	 * Attempts to retrieve a Metadata object from some service and returns
	 * a result to the user's observer.  
	 * 
	 * @param id the GUID of a MetaData object
	 * @param timeout time in milliseconds before operation times out
	 */
	void retrieve(String id, long timeout);
	
	/**
	 * Attempts to retrieve a Metadata object from some service for a collection
	 * of GUIDs as a batch operation and returns a result to the user's observer.  
	 * <p>
	 * Implementation default timeout used.
	 * 
	 * @param ids the collection of GUIDs 
	 */
	void retrieve(Collection<String> ids);
	
	/**
	 * Attempts to retrieve a Metadata object from some service for a collection
	 * of GUIDs as a batch operation and returns a result to the user's observer. 
	 * 
	 * @param ids the collection of GUIDs 
	 * @param timeout time in milliseconds before operation times out
	 */
	void retrieve(Collection<String> ids, long timeout);
	
}
