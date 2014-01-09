package com.barchart.feed.api.series;

import org.joda.time.DateTime;

public interface ITimeFrame {

	/**
	 * Returns the meta characteristics of this {@link ITimeFrame}
	 * 
	 * @return	the meta characteristics of this {@link ITimeFrame}
	 */
	public Period getPeriod();

	/**
	 * Returns the start date.
	 * 
	 * @return	the start date.
	 */
	public DateTime getStartDate();

	/**
	 * The ending date of this {@code ITimeFrame}
	 * 
	 * @return	ending date of this {@code ITimeFrame}
	 */
	public DateTime getEndDate();

	/**
	 * Tests the "derivability" data defined by this {@code ITimeFrame}
	 * to see if there is a chance of sharing between {@link Node}s 
	 * defined by both.
	 * 
	 * @param other		the frame being tested for source compatibility
	 * @return	true if compatible / derivable of false if not.
	 */
	public boolean isDerivableFrom(ITimeFrame other);

}