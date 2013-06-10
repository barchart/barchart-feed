package com.barchart.feed.api.timeseries;

import java.util.Date;
import java.util.Iterator;

/**
 * Ordered time series of aggregated bars for a traded instrument.
 */
public interface TimeSeries {

	/**
	 * The bar aggregation
	 */
	public BarAggregation aggregation();

	/**
	 * The earliest date available in this time series
	 */
	public Date start();

	/**
	 * The latest date available in this time series
	 */
	public Date end();

	/**
	 * Number of bars currently loaded.
	 */
	public int size();

	/**
	 * Indexed list of bars, 0 = oldest.
	 */
	public TimeSeriesBar bar(int index);

	/**
	 * The bar at the specified date (can be null if non-existent)
	 */
	public TimeSeriesBar bar(Date date);

	/**
	 * Bar iterator, most recent bar first
	 */
	public Iterator<TimeSeriesBar> iterator();

	/**
	 * Bar reverse iterator, oldest bar first
	 */
	public Iterator<TimeSeriesBar> reverseIterator();

	/**
	 * Backfill the existing data set with an additional number of bars
	 */
	public TimeSeriesFuture backfill(int bars);

	/**
	 * Backfill the existing data set from the specified from date
	 */
	public TimeSeriesFuture backfill(Date from);

	/**
	 * Add a listener for streaming updates. Events will fire on new updates
	 * only, not initial fill or backfill requests.
	 */
	public void addListener(TimeSeriesListener listener);

	/**
	 * Remove a listener
	 */
	public void removeListener(TimeSeriesListener listener);

}