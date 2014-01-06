package com.barchart.feed.api.series;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;


/**
 * Contains the bar data.
 * 
 * @param <T>   core value-util type
 */
public interface Bar extends TimePoint, Range {

	/**
	 * Returns the open price
	 * @return the open price
	 */
	public Price getOpen();

	/**
	 * Returns the close price
	 * @return the close price
	 */
	public Price getClose();

	/**
	 * Returns the volume
	 * @return the volume
	 */
	public Size getVolume();

	/**
	 * Returns the open interest (futures only)
	 * @return the open interest
	 */
	public Size getOpenInterest();
	
	/**
	 * Merges the specified <@link Bar> with this one, possibly updating any
	 * barrier elements (i.e. High, Low, etc) given the underlying type. Used for
	 * aggregating information based on {@link PeriodType}
	 * 
	 * Returns a boolean indicating whether this time point should be closed - refusing
	 * any subsequent merges. If this Bar should be closed, this method returns
	 * true, false if not.
	 *  
	 * @param other			the other Bar to merge.
	 * @param advanceTime	true if the time should also be merged, false if not
	 */
	public <E extends Bar> void merge(E other, boolean advanceTime);
}