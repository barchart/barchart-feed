package com.barchart.feed.api.timeseries;

import java.util.Date;

/**
 * A fluent interface for constructing time series queries.
 */
public interface TimeSeriesQuery {

	/**
	 * The instrument requested. Queries should request only one of an
	 * instrument, symbol or expression. Subsequent calls will overwrite the
	 * previous value.
	 */
	//public TimeSeriesQuery instrument(Instrument instrument);

	/**
	 * The symbol requested. Queries should request only one of an instrument,
	 * symbol or expression. Subsequent calls will overwrite the previous value.
	 */
	public TimeSeriesQuery symbol(String symbol);

	/**
	 * The bar aggregation
	 */
	public TimeSeriesQuery aggregation(Period aggregation);

	/**
	 * The start date (earliest bar)
	 */
	public TimeSeriesQuery start(Date start);

	/**
	 * The end date (latest bar)
	 */
	public TimeSeriesQuery end(Date end);

	/**
	 * The number of bars to request. When used in conjunction with start date,
	 * it specifies the number of bars in the future to retrieve. When used with
	 * end date, it specifies the numbers of bars in the past to retrieve. When
	 * used with both start and end dates, it requests an extra number of bars
	 * before the start date, which is useful for guaranteeing enough data for
	 * technical studies on fixed time range charts.
	 */
	public TimeSeriesQuery bars(int count);

	/**
	 * Include the specified sale conditions (only applies to tick/minute time
	 * series)
	 */
	public TimeSeriesQuery condition(SaleCondition... session);

	// STOCKS ONLY

	/**
	 * Include the specified stock events (stocks only)
	 */
	public TimeSeriesQuery event(Event... events);

	// FUTURES ONLY

	/**
	 * Set the continuation policy for futures charts
	 */
	public TimeSeriesQuery continuationPolicy(ContinuationPolicy policy);

	/**
	 * The "nearest month" offset for ContinuationPolicy.NEAREST time series
	 * requests (defaults to 1, the front month)
	 */
	public TimeSeriesQuery nearestOffset(int offset);

	/**
	 * The volume type to return for futures time series (single-contract, or
	 * total of all contracts for a root)
	 */
	public TimeSeriesQuery volume(VolumeType type);

}
