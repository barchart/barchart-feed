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
}