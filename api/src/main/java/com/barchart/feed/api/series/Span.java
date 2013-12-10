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
	
	/**
	 * Expands the lower and upper bounds of this {@code Span} to
	 * include the extremes of the specified Span if not already
	 * included.
	 * 
	 * @param span
	 * @return the union of this Span and the specified Span
	 */
	public Span union(Span span);
	
	/**
	 * Returns a {@code Span} whose range is the intersection of this
	 * {@code Span} and the specified Span.
	 * 
	 * @param	span  the Span with which to combine to produce an intersection.
	 * @return	a Span containing the interecting range of this Span and the 
	 * 			Span specified.
	 */
	public Span intersection(Span span);
}
