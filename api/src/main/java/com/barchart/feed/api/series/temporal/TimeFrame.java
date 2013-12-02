package com.barchart.feed.api.series.temporal;

import org.joda.time.DateTime;

/**
 * Basic time unit aggregation used during comparison of participants
 * in network composition or chaining of nodes.
 * 
 * @author David Ray
 */
public class TimeFrame {
	/** Meta characteristics of this {@code TimeFrame} */
	private Period period;
	/** The start date */
	private DateTime startDate;
	/** The last date the series should contain */
	private DateTime endDate;
	
	/**
	 * Constructs a new {@code TimeFrame}
	 * 
	 * @param period
	 * @param startDate
	 */
	public TimeFrame(Period period, DateTime startDate, DateTime endDate) {
		this.period = period;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Returns the meta characteristics of this {@link TimeFrame}
	 * 
	 * @return	the meta characteristics of this {@link TimeFrame}
	 */
	public Period getPeriod() {
		return period;
	}

	/**
	 * Returns the start date.
	 * 
	 * @return	the start date.
	 */
	public DateTime getStartDate() {
		return startDate;
	}
	
	/**
	 * The ending date of this {@code TimeFrame}
	 * 
	 * @return	ending date of this {@code TimeFrame}
	 */
	public DateTime getEndDate() {
		return endDate;
	}
}
