package com.barchart.feed.api.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/**
 * Contains the bar data.
 */
public interface Bar extends Range {

	public enum GREEK_TYPE {
		DELTA, THETA, GAMMA, VEGA, RHO
	}

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
	 * Returns the settlement price
	 *
	 * @return the settlement price
	 */
	public Price getSettlement();

	/**
	 * Returns the ask size at close.
	 */
	public Size getLastSize();

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
	 * Returns the volume. For aggregated bars this will be an average value.
	 *
	 * @return the volume
	 */
	public Size getVolume();

	/**
	 * Returns the volume traded up. For aggregated bars this will be an average
	 * value.
	 *
	 * @return the volume traded up.
	 */
	public Size getVolumeUp();

	/**
	 * Returns the volume traded down. For aggregated bars this will be an
	 * average value.
	 *
	 * @return the volume traded down.
	 */
	public Size getVolumeDown();

	/**
	 * Returns the calls volume of the underlying
	 *
	 * @return the volume
	 */
	public Size getCallsVolume();

	/**
	 * Returns the puts volume of the underlying
	 *
	 * @return the volume
	 */
	public Size getPutsVolume();

	/**
	 * Returns the traded value as the sum of all trade price * size. For aggregated bars this will be an average value.
	 */
	public Price getTradedValue();

	/**
	 * Returns the positive traded value. For aggregated bars this will be an
	 * average value.
	 */
	public Price getTradedValueUp();

	/**
	 * Returns the negative traded value. For aggregated bars this will be an
	 * average value.
	 */
	public Price getTradedValueDown();

	/**
	 * Returns the total number of trades contributing to this {@code Bar}
	 *
	 * @return the number of the trades in this bar.
	 * @see #merge(Bar, boolean)
	 */
	public Size getTradeCount();

	/**
	 * Returns the average open interest (futures only).
	 *
	 * @return the open interest
	 */
	public Size getOpenInterest();

	/**
	 * Returns all the calls open interest (underlying).
	 *
	 * @return the open interest
	 */
	public Size getCallsOpenInterest();

	/**
	 * Returns all the puts open interest (underlying).
	 *
	 * @return the open interest
	 */
	public Size getPutsOpenInterest();

	/**
	 * Returns the day of last trade for the option. This is not expiration day
	 *
	 * @return
	 */
	public DateTime getLastTradeDay();

	/**
	 * Returns the greek per requested type
	 *
	 * @return
	 */
	public Price getGreeks(GREEK_TYPE type);

	public Price getHistoricalVolatility();

	public Price getImpliedVolatility();

	public Price getTheoreticalPrice();

	public Price getUnderlying();

	/**
	 * Merges the specified <@code Bar> with this one, possibly updating any barrier elements (i.e. High, Low, etc)
	 * given the underlying type. Used for aggregating information based on {@link PeriodType}
	 *
	 * Returns a boolean indicating whether this time point should be closed - refusing any subsequent merges. If this
	 * Bar should be closed, this method returns true, false if not.
	 *
	 * @param other the other Bar to merge.
	 * @param advanceTime true if the time should also be merged, false if not
	 */
	public <E extends Bar> void merge(E other, boolean advanceTime);
}