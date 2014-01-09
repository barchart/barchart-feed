package com.barchart.feed.api.series;

import com.barchart.util.value.api.Time;

/**
 * Indicates a duration of time starting at {@link DataPoint#getTime()} and
 * ending at {@link #getNextTime()}.
 * 
 * @author David Ray
 */
public interface Span extends DataPoint {
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
	 * its parent {@link DataSeries}
	 * 
	 * @return	the starting index of this {@code Span}
	 */
	public int getIndex();
	
	/**
	 * Returns true if the specified span start time less than 
	 * this start time or a next time greater than this start time.
	 * 
	 * @param span     the span tested for extends quality
	 * @return         true if so, false if not
	 */
	public <T extends Span> boolean extendsSpan(T span);
	
	/**
	 * Expands the lower and upper bounds of this {@code Span} to
	 * include the extremes of the specified Span if not already
	 * included.
	 * 
	 * @param span
	 * @return the union of this Span and the specified Span
	 */
	public <T extends Span> T union(T span);
	
	/**
	 * Returns a {@code Span} whose range is the intersection of this
	 * {@code Span} and the specified Span.
	 * 
	 * @param	span  the Span with which to combine to produce an intersection.
	 * @return	a Span containing the interecting range of this Span and the 
	 * 			Span specified.
	 */
	public <T extends Span> T intersection(T span);
}
