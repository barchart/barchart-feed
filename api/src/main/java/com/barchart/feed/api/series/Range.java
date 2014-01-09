package com.barchart.feed.api.series;

import com.barchart.util.value.api.Price;

/**
 * Represents a bounded range of values between a given low and high
 * value at a specific time index.
 * 
 * @author David Ray
 */
public interface Range extends DataPoint {
	/**
	 * Returns the high value at this {@code Range}'s time index.
	 * @return	the high value at this {@code Range}'s time index.
	 */
	public Price getHigh();

	/**
	 * Returns the low value at this {@code Range}'s time index.
	 * @return	the low value at this {@code Range}'s time index.
	 */
	public Price getLow();
	
}
