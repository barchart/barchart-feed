package com.barchart.feed.api.series.service;

import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.TradingSession;
import com.barchart.feed.api.series.temporal.TradingWeek;

/**
 * A fluent interface for constructing time series queries.
 */
public interface Query {
    /**
     * Returns a flag indicating whether this Query has a 
     * custom query configured.
     * 
     * @return  true if so, false if not.
     */
    public boolean hasCustomQuery();
    /**
     * Returns the custom query configured.
     * @return
     */
    public String getCustomQuery();
    /**
     * Transforms this {@code Query} to a more robust {@link Subscription} type.
     * 
     * @param   i     the {@code Instrument}
     * @return        this Query transformed to a {@link Subscription}.
     */
    public Subscription toSubscription(Instrument i);
    /**
	 * Returns the analytic name requested. Queries should request only one of an instrument/
	 * symbol and one of either an expression or analytic. Subsequent calls will overwrite the previous value.
	 * @return	this query
	 */
	public String getAnalyticSpecifier();
	/**
	 * Returns the the symbol or expression requested. 
	 * Subsequent calls will overwrite the previous value.
	 * @return	this query
	 */
	public List<String> getSymbols();
	/**
	 * Returns the {@link Period} ({@link TimePoint} aggregation)
	 * @return	the {@link Period}
	 */
	public List<Period> getPeriods();
	/**
	 * Returns the start date (earliest bar)
	 * @return	the start date
	 */
	public DateTime getStart();
	/**
	 * Returns the end date (latest bar)
	 * @return	the end date
	 */
	public DateTime getEnd();
	/**
	 * The number of bars to request. When used in conjunction with end date,
	 * it specifies the number of bars in the future to retrieve. When used with
	 * start date, it specifies the numbers of bars in the past to retrieve. When
	 * used with both start and end dates, it requests an extra number of bars
	 * before the start date, which is useful for guaranteeing enough data for
	 * technical studies on fixed time range charts.
	 * 
	 * 
	 * @return	the padding
	 */
	public int getPadding();

	// FUTURES ONLY

	/**
	 * Returns the {@link ContinuationPolicy} for futures charts
	 * 
	 * @return	the {@link ContinuationPolicy}
	 */
	public ContinuationPolicy getContinuationPolicy();
	/**
	 * The "nearest month" offset for ContinuationPolicy.NEAREST time series
	 * requests (defaults to 1, the front month)
	 *
	 * @return	the nearest offset for the {@link ContinuationPolicy}
	 */
	public int getNearestOffset();
	/**
	 * Returns the volume type to return for futures time series (single-contract, or
	 * total of all contracts for a root)
	 * 
	 * @return	the {@link VolumeType}
	 */
	public VolumeType getVolumeType();
	/**
	 * Returns the {@link TradingWeek} which is a collection of {@link TradingSession}s comprising an average 
	 * week of trading.
	 * 
	 * @return	the {@link TradingWeek}
	 */
	public TradingWeek getTradingWeek();

}
