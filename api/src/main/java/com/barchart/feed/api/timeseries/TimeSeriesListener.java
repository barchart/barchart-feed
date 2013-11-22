package com.barchart.feed.api.timeseries;

public interface TimeSeriesListener {

	/**
	 * Notification that a bar was created or updated
	 */
	public void onBar(TimeSeriesBar bar, TimeSeries series);

}
