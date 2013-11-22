package com.barchart.feed.api.timeseries;

import java.util.Date;
import java.util.Iterator;

import org.joda.time.DateTime;

/**
 * Ordered collection/series of sequential time-based {@link DataPoint}s.
 * Provides specialized methods to search and retrieve data 
 * within this series in an optimized manner.
 */
public interface TimeSeries<T extends DataPoint> {

	/**
	 * Returns the {@link Period} which defines the aggregation of
	 * time and data for every {@link DataPoint}.
	 * 
	 * @return this {@code TimeSeries}' {@link Period}
	 */
	public Period getPeriod();

	/**
	 * Returns the earliest date available in this time series.
	 * 
	 * @return the earliest date available in this time series.
	 */
	public DateTime getStart();

	/**
	 * Returns the most recent date available in this time series.
	 * 
	 * @return	the most recent date available in this time series
	 */
	public DateTime getEnd();

	/**
	 * Returns the number of {@link DataPoint}s currently contained within this series.
	 * 
	 * @return	the size of this series
	 */
	public int size();

	/**
	 * Indexed list of bars, 0 = oldest.
	 */
	public TimeSeriesBar get(int index);

	/**
	 * The bar at the specified date (can be null if non-existent)
	 */
	public TimeSeriesBar bar(DateTime date);

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