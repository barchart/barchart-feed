package com.barchart.feed.series;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.Period;
import com.barchart.util.value.api.Existential;

/**
 * Abstraction of a {@link DataPointImpl} with custom value fields.
 *
 * @author David Ray
 *
 * @param <K>		an object acting as key
 * @param <V>		the subclass of Existential which must be some value api entity
 */
public abstract class GenericPoint<K, V extends Existential> extends DataPointImpl {

	protected GenericPoint(Period period, DateTime d) {
		super(period, d);
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
	public <E extends DataPoint> int compareTo(E other) {
		return  period.getPeriodType().compareAtResolution(date, ((DataPointImpl)other).date);
	}
}
