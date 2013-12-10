package com.barchart.feed.api.series;

import com.barchart.util.value.api.Time;

/**
 * Indicates a duration of time starting at {@link TimePoint#getTime()} and
 * ending at {@link #getNextTime()}.
 * 
 * @author David Ray
 */
public interface Span extends TimePoint {
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
	
	/**
	 * Return the specific index of this {@code Span} within
	 * its parent {@link TimeSeries}
	 * 
	 * @return	the starting index of this {@code Span}
	 */
	public int getIndex();
}
