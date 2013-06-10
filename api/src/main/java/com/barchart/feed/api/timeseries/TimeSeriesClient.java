package com.barchart.feed.api.timeseries;

import java.util.Date;

/**
 * First pass at historical data API.
 * 
 * @author jeremy
 */
public interface TimeSeriesClient {

	// Standard time series retrieval

	public TimeSeriesQuery newQuery();

	public TimeSeriesFuture fetch(TimeSeriesQuery query);

	public TimeSeriesFuture subscribe(TimeSeriesQuery query);

	// Forward Curves

	public TimeSeriesFuture fetchForwardCurve(String root);

	public TimeSeriesFuture fetchForwardCurve(String root, Date from);

	public TimeSeriesFuture subscribeForwardCurve(String root);

}