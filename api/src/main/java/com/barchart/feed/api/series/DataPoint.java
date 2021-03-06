package com.barchart.feed.api.series;

import org.joda.time.DateTime;

public interface DataPoint {

	/**
	 * Returns the date/time index of this {@code DataPoint}
	 * 
	 * @return the date/time index of this {@code DataPoint}
	 */
	public DateTime getDate();

	/**
	 * Returns the {@link Period} (aggregation interval and units) of this
	 * {@code TimePoint}
	 *
	 * @return	the {@link Period}
	 */
	public Period getPeriod();

	/**
	 * Compares this {@code DataPoint} to the argument returning an integer
	 * according to the {@link Comparable} contract. Additionally, the comparison
	 * made is AT THE RESOLUTION of this {@code DataPoint} as specified by the
	 * {@link Period} specified at time of construction.
	 *
	 * NOTE:
	 * The comparison made is according to THIS {@code DataPoint}'s {@code PeriodType}
	 * ONLY, thus disregarding the granularity of the {@code  PeriodType} set on
	 * the argument {@code DataPoint}.
	 */
	public <E extends DataPoint> int compareTo(E other);

}