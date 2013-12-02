package com.barchart.feed.api.series.temporal;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalTime;

/**
 * Represents a period of time within which a session
 * of trading occurs.
 * 
 * @author David Ray
 */
public class TradingSession implements Comparable<TradingSession> {
    /** The day of the week this trading session occurs on. */
    private int day;
    /** The day of the week this trading session ends on. */
    private int endDay;
    /** The start time of this trading session. */
    private LocalTime start;
    /** The time this trading session ends. */
    private LocalTime end;
    
    /** The number of milliseconds in a day. */
    private static final int MILLIS_IN_DAY = 86400000;
    
    /** Milliseconds in this TradingSession */
    private final long SESSION_MILLIS;
    
    
    /**
     * Constructs a new {@code TradingSession} object.
     * 
     * @param day           the day of the week this trading session occurs on.
     * @param endDay        the day of the week this trading session ends on.
     * @param start         the start time of this trading session.
     * @param end           the time this trading session ends.
     */
    public TradingSession(int day, int endDay, LocalTime start, LocalTime end) {
        this.day = day;
        this.endDay = endDay;
        this.start = start;
        this.end = end;
        SESSION_MILLIS = sessionMillis(day, start, endDay, end);
    }
    
    public int hourCount() {
        return Hours.hoursBetween(start, end).getHours();
    }
    
    /**
     * Returns this {@code TradingSession}'s day
     * represented as an int.
     * @return  this {@code TradingSession}'s day represented as an int.
     */
    public int day() {
        return day;
    }
    
    /**
     * Returns this {@code TradingSession}'s end day
     * represented as an int.
     * @return  this {@code TradingSession}'s end day
     *          represented as an int.
     */
    public int endDay() {
        return endDay;
    }
    
    /**
     * Returns this {@code TradingSession}'s start time.
     * @return  this {@code TradingSession}'s start time.
     */
    public LocalTime start() {
        return start;
    }
    
    /**
     * Returns this {@code TradingSession}'s end time.
     * @return  this {@code TradingSession}'s end time.
     */
    public LocalTime end() {
        return end;
    }
    
    /**
     *  Returns the number of milliseconds in this TradingSession.
     * @return  the number of milliseconds in this TradingSession.
     */
    public long sessionMillis() {
        return SESSION_MILLIS;
    }
    
    /**
     * Returns the number of milliseconds between the specified dates -
     * within this session.
     * 
     * @param startDate     the start date.
     * @param endDate       the end date.
     * @return      the number of milliseconds between the specified dates.
     */
    public static long sessionMillis(DateTime startDate, DateTime endDate) {
        return sessionMillis(startDate.getDayOfWeek(), startDate.toLocalTime(),
            endDate.getDayOfWeek(), endDate.toLocalTime());
    }
    
    /**
     * Returns the number of milliseconds between the specified days given
     * their respective start and end times. This method also accounts for
     * the rollover if the endDay is less than the startDay.
     * 
     * @param startDay      the starting day which the startTime is applied to.
     * @param startTime     the starting time on the start day specified.
     * @param endDay        the ending day which the endTime is applied to.
     * @param endTime       the ending time on the end day specified.
     * @return      the number of milliseconds between the start time on the 
     *              start day, and the end time on the end day.
     */
    public static long sessionMillis(int startDay, LocalTime startTime, int endDay, LocalTime endTime) {
        DateTime dt = startTime.toDateTimeToday().withDayOfWeek(startDay);
        DateTime dt2 = new DateTime(dt);
        if(TradingWeek.naturalDaysBetween(startDay, endDay) > 0) {
            dt2 = dt.millisOfDay().withMaximumValue().plusMillis(1);
            dt2 = dt2.plusMillis((TradingWeek.naturalDaysBetween(startDay, endDay) - 1) * MILLIS_IN_DAY);
            
            //BUG!!! when startDay == SUNDAY - adds one hour for some reason??? (so we subtract it here)
            //dt2 = startDay == DateTimeConstants.SUNDAY ? dt2.minusMillis(3600000) : dt2;
            dt2 = dt2.plusMillis(endTime.getMillisOfDay());
        }else{
            dt2 = dt2.plusMillis(endTime.getMillisOfDay() - startTime.getMillisOfDay());
        }
        
        return ExtendedChronology.getInstance().millis().getDifference(
            dt2.getMillis(), dt.getMillis());
    }
    
    /**
     * Convenience method to return the day of week of this {@code TradingSession}.
     * @return  this TradingSession's day in string form.
     */
    public String dayToString() {
        return TradingWeek.dayToString(day);
    }
    
    /**
     * Returns a flag indicating whether the specified date is contained
     * within this {@code TradingSession}
     * 
     * @param   date  the query date.
     * @return  true if the specified date is within this session, false if not.
     */
    public boolean contains(DateTime date) {
        int otherDay = date.getDayOfWeek();
        LocalTime lt = date.toLocalTime();
        
        boolean startBounded = (lt.isEqual(start) || lt.isAfter(start));
        boolean endBounded = (lt.isEqual(end) || lt.isBefore(end));
        return dayIsWithinSessionDay(otherDay) && 
            ((startBounded && endBounded) || 
             (day != endDay && 
                ((otherDay == day && startBounded) || 
                 (otherDay == endDay && endBounded) ||
                 dayIsBetweenStartEndDays(otherDay))
            ));
    }
    
    /**
     * Helper method to determine session containment conditions
     * in the advent of session wrapping.
     * 
     * @param otherDay  the day to compare.
     * @return          flag indicating true if day falls within session, false if not.
     */
    private boolean dayIsWithinSessionDay(int otherDay) {
        return (otherDay >= day && otherDay <= endDay) || 
            (endDay < day && (otherDay >= day || otherDay <= endDay)); 
    }
    
    /**
     * Returns true if the specified day is "strictly" between the
     * start and end days of this session, and <em>not</em> equal to
     * either the start or end days - false if not.
     * @param otherDay  the Day integer to test.
     * @return          flag indicating whether the specified day is "strictly" between the
     *                  start and end days of this session
     */
    private boolean dayIsBetweenStartEndDays(int otherDay) {
        return (otherDay > day && otherDay < endDay) || 
            (endDay < day && (otherDay > day || otherDay < endDay));
    }
    
    /**
     * Implementation of the {@link Comparable} interface.
     * @param other the other {@code TradingSession}
     * @return the int comparison value.
     */
    @Override
    public int compareTo(TradingSession other) {
        if(day == other.day && endDay == other.endDay) {
            return start.compareTo(other.start); 
        }else if(endDay < other.day) {
            return -1;
        }
        return 1;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + day;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + endDay;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TradingSession other = (TradingSession) obj;
        if (day != other.day)
            return false;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (endDay != other.endDay)
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return dayToString() + "(" + start + "-" + end + ")";
    }
}
