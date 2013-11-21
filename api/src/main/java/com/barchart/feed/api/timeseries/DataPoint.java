package com.barchart.feed.api.timeseries;

import org.joda.time.DateTime;


/**
 * An abstract representation of any time-based market data related to a specific
 * point in time.
 * 
 * @author David Ray
 */
public interface DataPoint extends Comparable<DataPoint> {
	/**
	 * Returns the time index of this {@code DataPoint}
	 * @return	the time index of this {@code DataPoint}
	 */
	public DateTime getDate();
	
	/**
     * Compares this {@code DataPoint} to the argument returning an integer
     * according to the {@link Comparable} contract. Additionally, the comparison
     * made is AT THE RESOLUTION of this {@code DataPoint} as specified by the
     * {@link Period} specified at time of construction. 
     * 
     * NOTE:
     * The comparison made is according to THIS {@code DataPoint}'s {@code TemporalType}
     * ONLY, thus disregarding the granularity of the {@code TemporalType} set on
     * the argument {@code DataPoint}.
     */
	@Override
	public int compareTo(DataPoint dataPoint);
}
