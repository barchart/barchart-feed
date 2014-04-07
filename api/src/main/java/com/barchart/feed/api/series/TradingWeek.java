package com.barchart.feed.api.series;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public interface TradingWeek {

	/**
	 * Returns the number of {@link TradingSession}s within this {@code ITradingWeek}.
	 * 
	 * @return  the number of {@link TradingSession}s within this {@code ITradingWeek}.
	 */
	public int length();
	
	/**
	 * Adds a new {@link TradingSession} to this {@code ITradingWeek}
	 * @param session
	 */
	public void addTradingSession(TradingSession session);
	
	/**
	 * Returns the first {@link TradingSession} in this {@code ITradingWeek}.
	 * 
	 * @return the first {@link TradingSession} in this {@code ITradingWeek}.
	 */
	public TradingSession getStartSession();

	/**
	 * Returns the last {@link TradingSession} in this {@code ITradingWeek}.
	 * 
	 * @return the last {@link TradingSession} in this {@code ITradingWeek}.
	 */
	public TradingSession getEndSession();

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
	 * trading day as defined by this {@link TradingWeek}'s holiday calendar.
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
     * Returns the {@link TradingSession} containing the specified date, or 
     * the immediately previous {@code TradingSession} after the specified {@link DateTime}.
     * 
     * @param date  the date on or before the returned {@link TradingSession}'s date.
     * @return      the {@link TradingSession} containing the specified date, or 
     *              the very next {@code TradingSession} after the specified {@link DateTime}.
     * @see #getTradingSessionOnOrAfter(DateTime)
     */
    public TradingSession getTradingSessionOnOrBefore(DateTime date);

	/**
	 * Returns the {@link TradingSession} containing the specified date, or 
	 * the very next {@code TradingSession} after the specified {@link DateTime}.
	 * 
	 * @param date  the date on or before the returned {@link TradingSession}'s date.
	 * @return      the {@link TradingSession} containing the specified date, or 
	 *              the very next {@code TradingSession} after the specified {@link DateTime}.
	 * @see #getTradingSessionOnOrBefore(DateTime)
	 */
	public TradingSession getTradingSessionOnOrAfter(DateTime date);
	
	/**
	 * Returns the date immediately previous to the specified data that is within the 
	 * boundaries of a {@link TradingSession} defined by this {@code TradingWeek}, using
	 * the granularity of the specified {@link Period}
	 * 
	 * @param date      the date following the returned session date.
	 * @return          the date immediately previous to the specified data that is within the 
     *                  boundaries of a {@link TradingSession} defined by this {@code TradingWeek}, using
     *                  the granularity of the specified {@link Period}
     * @see #getNextSessionDate(DateTime, Period)
	 */
	public DateTime getPreviousSessionDate(DateTime date, Period period);

	/**
	 * Returns the date immediately following the specified date that is within
	 * the boundaries of a {@link TradingSession} within this {@code TradingWeek},
	 * using the granularity of the specified {@link Period}.
	 * 
	 * @param dt       the date which the returned date will immediately follow.
	 * @param Period   the granularity with which to advance the specified date
	 *                 to find the next {@link TradingSession} date.
	 * @return  the date immediately following the specified date that is within
	 *          the boundaries of a {@link TradingSession} within this {@code TradingWeek},
	 *          using the granularity of the specified {@link Period}.
	 * @see #getPreviousSessionDate(DateTime)
	 */
	public DateTime getNextSessionDate(DateTime dt, Period period);

	/**
	 * Returns a flag indicating whether the specified date is a proper
	 * "trading day".
	 */
	public boolean isWorkingDay(LocalDate date);

}