package com.barchart.feed.api.series;

import com.barchart.util.value.api.Scaled;


/**
 * Contains the bar data.
 * 
 * @param <T>   core value-util type
 */
public interface Bar<T extends Scaled<T>> extends TimePoint, Range<T> {

	/**
	 * Returns the open price
	 * @return the open price
	 */
	public T getOpen();

	/**
	 * Returns the close price
	 * @return the close price
	 */
	public T getClose();

	/**
	 * Returns the volume
	 * @return the volume
	 */
	public T getVolume();

	/**
	 * Returns the open interest (futures only)
	 * @return the open interest
	 */
	public T getOpenInterest();
}