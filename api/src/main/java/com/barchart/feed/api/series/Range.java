package com.barchart.feed.api.series;

import com.barchart.util.value.api.Scaled;

/**
 * Represents a bounded range of values between a given low and high
 * value at a specific time index.
 * 
 * @author David Ray
 */
public interface Range<T extends Scaled<T>> extends TimePoint {
	/**
	 * Returns the high value at this {@code Range}'s time index.
	 * @return	the high value at this {@code Range}'s time index.
	 */
	public Scaled<T> getHigh();

	/**
	 * Returns the low value at this {@code Range}'s time index.
	 * @return	the low value at this {@code Range}'s time index.
	 */
	public Scaled<T> getLow();
	
}
