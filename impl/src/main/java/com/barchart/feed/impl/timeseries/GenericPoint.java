package com.barchart.feed.impl.timeseries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.timeseries.Period;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Time;

/**
 * Abstraction of a {@link DataPoint} with custom value fields.
 * 
 * @author David Ray
 *
 * @param <K>		an object acting as key
 * @param <V>		the subclass of Existential which must be some value api entity
 */
public abstract class GenericPoint<K, V extends Existential> extends DataPoint {
	
	protected GenericPoint(Period period, Time t) {
		super(period, t);
	}

	/** Holds the values of this {@code GenericPoint} */
	protected  Map<K, V> genericMap = Collections.synchronizedMap(new HashMap<K, V>());
	
	/**
	 * Allows the creation of {@code DataPoint}s containing custom
	 * data fields.
	 * 
	 * @param key			the identifier key for the custom field
	 * @param value			the value corresponding to the specified key
	 */
	public  void setValue(K key, V value) {
		
	}
	
	/**
	 * Returns the value specified key.
	 * 
	 * @param key			the identifier key for the custom field
	 * @return				the value corresponding to the specified key
	 */
	public V getValue(K key) { return null; }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract int compareTo(DataPoint dataPoint);
}
