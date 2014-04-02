package com.barchart.feed.api.series;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/**
 * Contains the bar data.
 */
public interface Bar extends Range {

	/**
	 * Return the instrument ID for this bar.
	 */
	public InstrumentID getInstrument();

	/**
	 * Returns the open price
	 *
	 * @return the open price
	 */
	public Price getOpen();

	/**
	 * Returns the close price
	 * 
	 * @return the close price
	 */
	public Price getClose();

	/**
	 * Returns the bid price at close.
	 */
	public Price getBid();

	/**
	 * Returns the bid size at close.
	 */
	public Size getBidSize();

	/**
	 * Returns the ask price at close.
	 */
	public Price getAsk();

	/**
	 * Returns the ask size at close.
	 */
	public Size getAskSize();

	/**
	 * Returns the midpoint price (either a high/low average or VWAP)
	 */
	public Price getMidpoint();

	/**
	 * Returns the volume
	 * 
	 * @return the volume
	 */
	public Size getVolume();

	/**
	 * Returns the volume traded up.
	 * 
	 * @return the volume traded up.
	 */
	public Size getVolumeUp();

	/**
	 * Returns the volume traded down.
	 *
	 * @return the volume traded down.
	 */
	public Size getVolumeDown();

	/**
	 * Returns the total traded value as the sum of all trade price * size
	 */
	public Price getTradedValue();

	/**
	 * Returns the total positive traded value
	 */
	public Price getTradedValueUp();

	/**
	 * Returns the total negative traded value
	 */
	public Price getTradedValueDown();

	/**
	 * Returns the total number of ticks (trades) contributing to this
	 * {@code Bar}
	 *
	 * @return the number of the ticks in this bar.
	 * @see #merge(Bar, boolean)
	 */
	public Size getTickCount();

	/**
	 * Returns the open interest (futures only)
	 *
	 * @return the open interest
	 */
	public Size getOpenInterest();

	/**
	 * Merges the specified <@code Bar> with this one, possibly updating any
	 * barrier elements (i.e. High, Low, etc) given the underlying type. Used
	 * for aggregating information based on {@link PeriodType}
	 *
	 * Returns a boolean indicating whether this time point should be closed -
	 * refusing any subsequent merges. If this Bar should be closed, this method
	 * returns true, false if not.
	 *
	 * @param other the other Bar to merge.
	 * @param advanceTime true if the time should also be merged, false if not
	 * @see #getTickCount()
	 */
	public <E extends Bar> void merge(E other, boolean advanceTime);
}