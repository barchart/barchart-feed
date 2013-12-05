package com.barchart.feed.api.series;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Time;


/**
 * Represents a "block" or two-dimensional range of time and it's representative high and low
 * values at either extreme.
 * 
 * @author David Ray
 */
public interface Area extends Range {
	/**
	 * Returns the high value at the future most time index.
	 * 
	 * @return	the high value at the future most time index.
	 */
	public Price getNextHigh();
	/**
	 * Returns the low value at the future most time index.
	 * 
	 * @return	the low value at the future most time index.
	 */
	public Price getNextlow();
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
