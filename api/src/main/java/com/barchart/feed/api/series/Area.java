package com.barchart.feed.api.series;

import com.barchart.util.value.api.Scaled;
import com.barchart.util.value.api.Time;


/**
 * Represents a "block" or two-dimensional range of time and it's representative high and low
 * values at either extreme.
 * 
 * @author David Ray
 */
public interface Area<T extends Scaled<T>> extends Range<T> {
	/**
	 * Returns the high value at the future most time index.
	 * 
	 * @return	the high value at the future most time index.
	 */
	public Scaled<T> getNextHigh();
	/**
	 * Returns the low value at the future most time index.
	 * 
	 * @return	the low value at the future most time index.
	 */
	public Scaled<T> getNextlow();
	/**
	 * Returns the future-most {@link Time}
	 * @return	the future-most {@link Time}
	 */
	public Time getNextTime();
	/**
	 * Returns the the future-most index.
	 * 
	 * @return	the the future-most index.
	 */
	public int getNextIndex();
}
