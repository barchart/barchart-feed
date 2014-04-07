package com.barchart.feed.api.series;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public interface TradingSession extends Comparable<TradingSession> {

	public int hourCount();

	/**
	 * Returns this {@code TradingSession}'s day
	 * represented as an int.
	 * @return  this {@code TradingSession}'s day represented as an int.
	 */
	public int day();

	/**
	 * Returns this {@code TradingSession}'s end day
	 * represented as an int.
	 * @return  this {@code TradingSession}'s end day
	 *          represented as an int.
	 */
	public int endDay();

	/**
	 * Returns this {@code TradingSession}'s start time.
	 * @return  this {@code TradingSession}'s start time.
	 */
	public LocalTime start();

	/**
	 * Returns this {@code TradingSession}'s end time.
	 * @return  this {@code TradingSession}'s end time.
	 */
	public LocalTime end();

	/**
	 *  Returns the number of milliseconds in this TradingSession.
	 * @return  the number of milliseconds in this TradingSession.
	 */
	public long sessionMillis();

	/**
	 * Convenience method to return the day of week of this {@code TradingSession}.
	 * @return  this TradingSession's day in string form.
	 */
	public String dayToString();

	/**
	 * Returns a flag indicating whether the specified date is contained
	 * within this {@code TradingSession}
	 * 
	 * @param   date  the query date.
	 * @return  true if the specified date is within this session, false if not.
	 */
	public boolean contains(DateTime date);

	/**
	 * Implementation of the {@link Comparable} interface.
	 * @param other the other {@code TradingSession}
	 * @return the int comparison value.
	 */
	public <T extends TradingSession> int compareTo(T other);

}