package com.barchart.feed.api.timeseries;

public class BarAggregation {

	public static final BarAggregation ONE_MINUTE = new BarAggregation(
			Period.MINUTE, 1);
	public static final BarAggregation FIVE_MINUTE = new BarAggregation(
			Period.MINUTE, 5);
	public static final BarAggregation FIFTEEN_MINUTE = new BarAggregation(
			Period.MINUTE, 15);
	public static final BarAggregation THIRTY_MINUTE = new BarAggregation(
			Period.MINUTE, 30);
	public static final BarAggregation SIXTY_MINUTE = new BarAggregation(
			Period.MINUTE, 60);
	public static final BarAggregation DAY = new BarAggregation(Period.DAY, 1);
	public static final BarAggregation WEEK =
			new BarAggregation(Period.WEEK, 1);
	public static final BarAggregation MONTH = new BarAggregation(Period.MONTH,
			1);
	public static final BarAggregation QUARTER = new BarAggregation(
			Period.MONTH, 3);
	public static final BarAggregation SIX_MONTH = new BarAggregation(
			Period.MONTH, 6);
	public static final BarAggregation YEAR =
			new BarAggregation(Period.YEAR, 1);

	public enum Period {
		MINUTE, DAY, WEEK, MONTH, YEAR
	};

	private final Period period;
	private final int size;

	protected BarAggregation(final Period period_, final int size_) {
		period = period_;
		size = size_;
	}

	/**
	 * The aggregation period
	 */
	public Period period() {
		return period;
	}

	/**
	 * The number of periods contained in one bar
	 */
	public int size() {
		return size;
	}

}
