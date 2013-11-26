package com.barchart.feed.api.timeseries;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Instrument;

/**
 * A fluent interface for constructing time series queries.
 */
public interface Query {

	/**
	 * The instrument requested. Queries should request only one of an
	 * instrument, symbol or expression. Subsequent calls will overwrite the
	 * previous value.
	 */
	public Query instrument(Instrument instrument);

	/**
	 * The symbol requested. Queries should request only one of an instrument,
	 * symbol or expression. Subsequent calls will overwrite the previous value.
	 */
	public Query symbol(String symbol);

	/**
	 * The bar aggregation
	 */
	public Query period(Period period);

	/**
	 * The start date (earliest bar)
	 */
	public Query start(DateTime start);

	/**
	 * The end date (latest bar)
	 */
	public Query end(DateTime end);

	/**
	 * The number of bars to request. When used in conjunction with end date,
	 * it specifies the number of bars in the future to retrieve. When used with
	 * start date, it specifies the numbers of bars in the past to retrieve. When
	 * used with both start and end dates, it requests an extra number of bars
	 * before the start date, which is useful for guaranteeing enough data for
	 * technical studies on fixed time range charts.
	 */
	public Query padding(int count);

	/**
	 * Include the specified sale conditions (only applies to tick/minute time
	 * series)
	 */
	public Query condition(SaleCondition... session);

	// STOCKS ONLY

	/**
	 * Include the specified stock events (stocks only)
	 */
	public Query event(Event... events);

	// FUTURES ONLY

	/**
	 * Set the continuation policy for futures charts
	 */
	public Query continuationPolicy(ContinuationPolicy policy);

	/**
	 * The "nearest month" offset for ContinuationPolicy.NEAREST time series
	 * requests (defaults to 1, the front month)
	 */
	public Query nearestOffset(int offset);

	/**
	 * The volume type to return for futures time series (single-contract, or
	 * total of all contracts for a root)
	 */
	public Query volume(VolumeType type);

}
