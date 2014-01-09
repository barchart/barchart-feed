package com.barchart.feed.api.series.temporal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public interface ITradingWeek {

	/**
	 * Returns the number of {@link ITradingSession}s within this {@code ITradingWeek}.
	 * 
	 * @return  the number of {@link ITradingSession}s within this {@code ITradingWeek}.
	 */
	public int length();
	
	/**
	 * Adds a new {@link ITradingSession} to this {@code ITradingWeek}
	 * @param session
	 */
	public void addTradingSession(ITradingSession session);
	
	/**
	 * Returns the first {@link ITradingSession} in this {@code ITradingWeek}.
	 * 
	 * @return the first {@link ITradingSession} in this {@code ITradingWeek}.
	 */
	public ITradingSession getStartSession();

	/**
	 * Returns the last {@link ITradingSession} in this {@code ITradingWeek}.
	 * 
	 * @return the last {@link ITradingSession} in this {@code ITradingWeek}.
	 */
	public ITradingSession getEndSession();

	/**
	 * Returns the number of valid trading days within the month
	 * of the specified date.
	 * 
	 * @param   dt  the date specifying the month in question.
	 * @return      the number of valid trading days within the month
	 *              of the specified date.
	 */
	public int getTradingDaysInMonth(DateTime dt);

	/**
	 * Returns a flag indicating whether the specified date is a valid
	 * trading day as defined by this {@link ITradingWeek}'s holiday calendar.
	 * 
	 * @param dt	the date to test
	 * @return	true if so, false if not.
	 */
	public boolean isTradingDay(DateTime dt);

	/**
	 * Returns the current date if it is both a valid working day and
	 * it is a day within this {@code ITradingWeek}'s configured session
	 * days, otherwise this method returns the very next date that satisfies
	 * the above conditions.
	 * 
	 * @param       dt    the date to start the search from.
	 * @return            the current date if it is a valid trading date
	 *                    or the very next valid trading date.
	 */
	public DateTime getSessionTradingDate(DateTime dt);

	/**
	 * Returns the number of milliseconds within this {@code ITradingWeek}'s 
	 * configured trading sessions.
	 * 
	 * @return  the number of milliseconds within this {@code ITradingWeek}'s 
	 *          configured trading sessions.
	 */
	public long getWeekMillis();

	/**
	 * Returns the number of <em>session</em> milliseconds between
	 * the two dates specified, skipping session breaks and other
	 * "non-trading" periods.
	 * 
	 * Assumes all dates and times specified exist within one of this ITradingWeek's
	 * sessions.
	 * 
	 * @param dt1       the first date
	 * @param dt2       the second date
	 * @return          the number of <em>session</em> milliseconds between
	 *                  the two dates specified 
	 */
	public long getSessionMillisBetween(DateTime dt1, DateTime dt2);

	/**
	 * Returns the {@link ITradingSession} containing the specified date, or 
	 * the very next {@code ITradingSession} after the specified {@link DateTime}.
	 * 
	 * @param date  the date on or before the returned {@link ITradingSession}'s date.
	 * @return      the {@link ITradingSession} containing the specified date, or 
	 *              the very next {@code ITradingSession} after the specified {@link DateTime}.
	 */
	public ITradingSession getTradingSessionOnOrAfter(DateTime date);

	/**
	 * Returns the date immediately following the specified date that is within
	 * the boundaries of a {@link ITradingSession} within this {@code ITradingWeek},
	 * using the granularity of the specified {@link Period}.
	 * @param dt            the date which the returned date will immediately follow.
	 * @param Period  the granularity with which to advance the specified date
	 *                      to find the next {@link ITradingSession} date.
	 * @return  the date immediately following the specified date that is within
	 *          the boundaries of a {@link ITradingSession} within this {@code ITradingWeek},
	 *          using the granularity of the specified {@link Period}.
	 */
	public DateTime getNextSessionDate(DateTime dt, Period period);

	/**
	 * Returns a flag indicating whether the specified date is a proper
	 * "trading day".
	 */
	public boolean isWorkingDay(LocalDate date);

}