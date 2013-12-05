package com.barchart.feed.api.series.temporal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.common.WorkingWeek;
import net.objectlab.kit.datecalc.joda.JodaWorkingWeek;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


/**
 * Represents the days of a given week in which trading occurs
 * in a particular financial market, for a given financial instrument.
 * TradingWeeks contain {@link TradingSession}s which in turn define the hours
 * of trading in a single trading day or contiguous span of trading
 * days. While a trading week need not contain trading days which are
 * all contiguous within a trading week, a given {@code TradingSession}
 * which spans more than 1 day must be contiguous within the hours of
 * that particular session. Said another way, a given {@code TradingSession}
 * cannot skip days - instead, another TradingSession should be constructed
 * such that a single day of trading may contain more than one TradingSession
 * among which, hours may be skipped <em><b>in between</b></em> sessions but
 * <b>NOT</b> within a session. 
 * <p>
 * For example:<br>
 * A TradingSession with a begin day of Sunday and an end day of Monday, must
 * contain contiguous hours beginning on Sunday and ending on Monday. If there
 * is a "break" in the session that spans past the day boundary of Sunday, 
 * then another session should be created such
 * that the first session should end before midnight Monday, and the second
 * session should begin at midnight or after on Monday.
 * <p>
 * TradingWeeks also are instrument specific, due to the possible variation in trading
 * hours between instruments. This class does not contain definitions for {@link Symbol}s
 * (a.k.a instruments), therefore care must be taken to associate a particular {@code Symbol}
 * with its proper corresponding {@code TradingWeek}.
 * 
 * 
 * @author David Ray
 */
public class TradingWeek extends JodaWorkingWeek {
	public enum LoadType { MEMORY, FILE };
	
    private long weekMillis = -1;
    
    private List<TradingSession> sessions = new ArrayList<TradingSession>();
    
    private DateCalculator<LocalDate> calculator;
    
    private static final Properties DEFAULT_PROPS; 
    public static final TradingWeek DEFAULT;
    static {
        DEFAULT_PROPS = new Properties();
        DEFAULT_PROPS.put("DEFAULT_CALENDAR.holidayDateFileLoadType", TradingWeek.LoadType.MEMORY);
        DEFAULT_PROPS.put("DEFAULT_CALENDAR.holidayDelimiter", ",");
        DEFAULT_PROPS.put("DEFAULT_CALENDAR.holidayDates", "1610-1-1");
        
        DEFAULT_PROPS.setProperty("DEFAULT_SESSIONS.sessionParamDelimiter", ",");
        DEFAULT_PROPS.setProperty("DEFAULT_SESSIONS.sessionDelimiter", ";");
        DEFAULT_PROPS.setProperty("DEFAULT_SESSIONS.sessions", "7,08:30:0:0,7,19:59:59:999;1,08:30:0:0,1,19:59:59:999;2,08:30:0:0,2,19:59:59:999;" +
            "3,08:30:0:0,3,19:59:59:999;4,08:30:0:0,4,19:59:59:999;5,08:30:0:0,5,19:59:59:999;6,08:30:0:0,6,19:59:59:999");
        
        try {
            DEFAULT = TradingWeek.configBuilder(DEFAULT_PROPS, "DEFAULT_CALENDAR", "DEFAULT_SESSIONS").build();
        } catch(Exception e) { throw new IllegalStateException("could not initialize default trading session."); }
    }
     
    
    
    /**
     * Instantiates a new TradingWeek
     * @param tradingDays
     */
    private TradingWeek(byte tradingDays) {
        super(tradingDays);
    }
    
    /**
     * Returns a "Builder" for building a {@code TradingWeek} from a specified
     * file path as a resource on the current classpath. 
     * 
     * @param propertiesFilePath            the part of a given file path which describes
     *                                      the location and name of a properties file located
     *                                      in the current classpath. If the directory containing
     *                                      a given filename is directly on the configured classpath,
     *                                      then the name of the file is all that is needed.
     * @param calendarName                  the name which can be used to identify a given calendar. Calendars
     *                                      are identified by their name and are reused once they are defined 
     *                                      and registered (this happens automatically, the user doesn't have
     *                                      to be concerned with the registration or reuse). Therefore each
     *                                      calendar which is considered unique, should have a unique name
     *                                      specified - though calendars may definitely be shared.
     * @param tradingWeekName               the name associated with a given {@code TradingWeek} and its 
     *                                      contained {@link TradingSession}s.
     *                                      
     * @return                              a {@link Builder} capable of returning a fully configured {@code TradingWeek}.
     * @throws IllegalArgumentException     if calendarName or tradingWeekName are null.
     * @throws IOException                  if there is a problem loading the properties file from the path specified. 
     */
    public static Builder configBuilder(
        String propertiesFilePath, String calendarName, String tradingWeekName) 
            throws IllegalArgumentException, IOException {
        return new ConfigBuilder(propertiesFilePath, calendarName, tradingWeekName);
    }
    
    /**
     * Returns a "Builder" for building a {@code TradingWeek} from a specified
     * file path as a resource on the current classpath. 
     * 
     * @param propertiesFilePath            the part of a given file path which describes
     *                                      the location and name of a properties file located
     *                                      in the current classpath. If the directory containing
     *                                      a given filename is directly on the configured classpath,
     *                                      then the name of the file is all that is needed.
     * @param calendarName                  the name which can be used to identify a given calendar. Calendars
     *                                      are identified by their name and are reused once they are defined 
     *                                      and registered (this happens automatically, the user doesn't have
     *                                      to be concerned with the registration or reuse). Therefore each
     *                                      calendar which is considered unique, should have a unique name
     *                                      specified - though calendars may definitely be shared.
     * @param tradingWeekName               the name associated with a given {@code TradingWeek} and its 
     *                                      contained {@link TradingSession}s.
     *                                      
     * @return                              a {@link Builder} capable of returning a fully configured {@code TradingWeek}.
     * @throws IllegalArgumentException     if calendarName or tradingWeekName are null.
     * @throws IOException                  if there is a problem loading the properties file from the path specified. 
     */
    public static Builder configBuilder(
        Properties properties, String calendarName, String tradingWeekName) 
            throws IllegalArgumentException, IOException {
        return new ConfigBuilder(properties, calendarName, tradingWeekName);
    }
    
    /**
     * Returns the number of {@link TradingSession}s within this {@code TradingWeek}.
     * 
     * @return  the number of {@link TradingSession}s within this {@code TradingWeek}.
     */
    public int length() {
        return sessions.size();
    }
    
    /**
     * Adds a {@link TradingSession} to this {@code TradingWeek}.
     * 
     * @param session   the {@code TradingSession} to add.
     */
    void addTradingSession(TradingSession session) {
        sessions.add(session);
        weekMillis = -1;
        setWeekMillis();
    }
    
    /**
     * Sets the {@link DateCalculator} this {@code TradingWeek} uses to
     * calculate its holidays and trading days.
     * 
     * @param calc  the {@link DateCalculator} this {@code TradingWeek} uses to
     *              calculate its holidays and trading days.
     */
    void setCalculator(DateCalculator<LocalDate> calc) {
        this.calculator = calc;
    }
    
    /**
     * Returns the first {@link TradingSession} in this {@code TradingWeek}.
     * 
     * @return the first {@link TradingSession} in this {@code TradingWeek}.
     */
    public TradingSession getStartSession() {
        return sessions.get(0);
    }
    
    /**
     * Returns the last {@link TradingSession} in this {@code TradingWeek}.
     * 
     * @return the last {@link TradingSession} in this {@code TradingWeek}.
     */
    public TradingSession getEndSession() {
        return sessions.get(sessions.size() - 1);
    }
    
    /**
     * Returns the number of valid trading days within the month
     * of the specified date.
     * 
     * @param   dt  the date specifying the month in question.
     * @return      the number of valid trading days within the month
     *              of the specified date.
     */
    public int getTradingDaysInMonth(DateTime dt) {
        //Get the first valid/configured trading date within the specified date's month
        dt = getSessionTradingDate(dt.dayOfMonth().withMinimumValue());
        
        int month = dt.getMonthOfYear();
        int sessionIdx = sessions.indexOf(getTradingSessionOnOrAfter(dt));
        int prevday = 0;
        int nextday = 0;
        int days = 0;
        int sessionLen = sessions.size();
        DateTime workingDate = new DateTime(dt);
        int tradingDayCount = 0;
        while(workingDate.getMonthOfYear() == month) {
            do {
                prevday = sessions.get(sessionIdx).day();
                if(++sessionIdx == sessionLen) {
                    sessionIdx = 0;
                }
                nextday = sessions.get(sessionIdx).day();
                
                days += naturalDaysBetween(prevday, nextday);
                workingDate = dt.plusDays(days);
            } while(calculator.isNonWorkingDay(workingDate.toLocalDate()) &&
                    workingDate.getMonthOfYear() == month);
            
            ++tradingDayCount;
        }
        
        return tradingDayCount;
    }
    
    public boolean isTradingDay(DateTime dt) {
    	return !this.calculator.isNonWorkingDay(dt.toLocalDate());
    }
    
    /**
     * Returns the current date if it is both a valid working day and
     * it is a day within this {@code TradingWeek}'s configured session
     * days, otherwise this method returns the very next date that satisfies
     * the above conditions.
     * 
     * @param       dt    the date to start the search from.
     * @return            the current date if it is a valid trading date
     *                    or the very next valid trading date.
     */
    public DateTime getSessionTradingDate(DateTime dt) {
        DateTime workingDate = new DateTime(dt);
        while(calculator.isNonWorkingDay(workingDate.toLocalDate()) ||
            !isWorkingDay(workingDate.toLocalDate())) {
            workingDate = workingDate.plusDays(1);
        }
        return workingDate;
    }
    
    /**
     * Called internally to update the current trading week's 
     * millisecond count.
     */
    private void setWeekMillis() {
        if(weekMillis == -1) {
            weekMillis = 0;
            for(TradingSession session : sessions) {
                weekMillis += session.sessionMillis();
            }
        }
    }
    
    /**
     * Returns the number of milliseconds within this {@code TradingWeek}'s 
     * configured trading sessions.
     * 
     * @return  the number of milliseconds within this {@code TradingWeek}'s 
     *          configured trading sessions.
     */
    public long getWeekMillis() {
        return weekMillis;
    }
    
    /**
     * Returns the number of <em>session</em> milliseconds between
     * the two dates specified, skipping session breaks and other
     * "non-trading" periods.
     * 
     * Assumes all dates and times specified exist within one of this TradingWeek's
     * sessions.
     * 
     * @param dt1       the first date
     * @param dt2       the second date
     * @return          the number of <em>session</em> milliseconds between
     *                  the two dates specified 
     */
    public long getSessionMillisBetween(DateTime dt1, DateTime dt2) {
        TradingSession currentSession = getTradingSessionOnOrAfter(dt1);
        int daysBetween = Days.daysBetween(dt1, dt2).getDays();
        long resultMillis = 0;
        if(currentSession.contains(dt2) && daysBetween == 0) {
            resultMillis = ExtendedChronology.getInstance().millis().
                getDifferenceAsLong(dt2.getMillis(), dt1.getMillis());
        }else{
            resultMillis = currentSession.end().getMillisOfDay() - dt1.getMillisOfDay();
            
            int weekDiff = daysBetween / 7;
            int len = sessions.size();
            TradingSession  endSession = getTradingSessionOnOrAfter(dt2);
            if(weekDiff > 0) {
                int sessionIdx = sessions.indexOf(currentSession);
                //Add the rest of the week's milliseconds.
                while(++sessionIdx < len) {
                    resultMillis += sessions.get(sessionIdx).sessionMillis();
                }
                
                //Add all the non-partial weeks (n - 2) in between the start date and end date.
                if(weekDiff > 1) {
                    resultMillis += getWeekMillis() * (weekDiff - 1);
                }
                
                //Add the sessions in the containing week up to the target end date.
                int daysFromWeekStart = sessions.indexOf(endSession);
                sessionIdx = -1;
                while(++sessionIdx < daysFromWeekStart) {
                    resultMillis += sessions.get(sessionIdx).sessionMillis();
                }
            }else{
                int sessionIdx = sessions.indexOf(currentSession);
                //Add the remaining days - 1 in milliseconds.
                int i = 0;
                while(++i < daysBetween) {
                    sessionIdx = sessionIdx < len - 1 ? sessionIdx + 1 : 0;
                    resultMillis += sessions.get(sessionIdx).sessionMillis();
                }
            }
            //Add the number of milliseconds from the start of the containing session
            //to the end time within the session.
            resultMillis += (dt2.getMillisOfDay() - endSession.start().getMillisOfDay());
        }
        return resultMillis;
    }
    
    /**
     * Returns the {@link TradingSession} containing the specified date, or 
     * the very next {@code TradingSession} after the specified {@link DateTime}.
     * 
     * @param date  the date on or before the returned {@link TradingSession}'s date.
     * @return      the {@link TradingSession} containing the specified date, or 
     *              the very next {@code TradingSession} after the specified {@link DateTime}.
     */
    public TradingSession getTradingSessionOnOrAfter(DateTime date) {
        if(sessions.size() == 0) {
            throw new IllegalStateException("Trading week has no sessions configured!");
        }
        while(!isWorkingDay(date.toLocalDate())) {
            date = date.plusDays(1);
        }
        int otherDay = date.getDayOfWeek();
        LocalTime lt = date.toLocalTime();
        int len = sessions.size();
        for(int i = 0;i < len;i++) {
            TradingSession ts = sessions.get(i);
            if(ts.contains(date)) {
                return ts;
            }else if(ts.day() == otherDay){
               if(ts.start().isAfter(lt)) {
                   return ts;
               }else if(ts.end().isBefore(lt)) {
                   return sessions.get(i == len - 1 ? 0 : i + 1);
               }
            }
        }
        return null;
    }
    
    /**
     * Returns the date immediately following the specified date that is within
     * the boundaries of a {@link TradingSession} within this {@code TradingWeek},
     * using the granularity of the specified {@link Period}.
     * @param dt            the date which the returned date will immediately follow.
     * @param Period  the granularity with which to advance the specified date
     *                      to find the next {@link TradingSession} date.
     * @return  the date immediately following the specified date that is within
     *          the boundaries of a {@link TradingSession} within this {@code TradingWeek},
     *          using the granularity of the specified {@link Period}.
     */
    public DateTime getNextSessionDate(DateTime dt, Period period) {
        if(dt == null) {
            return null;
        }
        int interval = period.size();
        PeriodType periodType = period.getPeriodType();
        dt = dt.millisOfSecond().withMinimumValue();
        TradingSession tradingSession = getTradingSessionOnOrAfter(dt);
        boolean skip = false;
        switch(periodType) {
            case YEAR: {
                dt = dt.plusYears(interval);
                skip = true;
            }
            case QUARTER: {
                if(!skip) {
                    dt = ExtendedChronology.withPeriodStart(dt).plusMonths(3 * interval);
                    skip = true;
                }
            }
            case MONTH: {
                if(periodType != PeriodType.QUARTER) {
                    dt = skip ? dt.monthOfYear().withMinimumValue() : dt.plusMonths(interval); 
                    skip = true;
                }
            }
            case WEEK: {
                if(!skip) {
                    dt = dt.plusWeeks(interval);
                    dt = dt.withDayOfWeek(getStartSession().day());
                    skip = true;
                }
            }
            case DAY: {
                if(periodType != PeriodType.WEEK) {
                    dt = skip ? dt.dayOfMonth().withMinimumValue() : dt.plusDays(interval);
                    tradingSession = getTradingSessionOnOrAfter(dt);
                    if(tradingSession.day() < dt.getDayOfWeek()) {
                        dt = dt.plusDays((DateTimeConstants.SUNDAY - dt.getDayOfWeek()) + 1);
                    }
                    dt = dt.withDayOfWeek(tradingSession.day());
                    skip = true;
                }
            }
            case HOUR: {
                if(skip) {
                    LocalTime lt = tradingSession.start();
                    dt = dt.withHourOfDay(lt.getHourOfDay());
                }else{
                    dt = dt.plusHours(interval);
                    tradingSession = getTradingSessionOnOrAfter(dt);
                    if(!tradingSession.contains(dt)) {
                        if(tradingSession.day() < dt.getDayOfWeek()) {
                            dt = dt.plusDays((DateTimeConstants.SUNDAY - dt.getDayOfWeek()) + 1);
                        }
                        dt = dt.withDayOfWeek(tradingSession.day());
                        dt = dt.withHourOfDay(tradingSession.start().getHourOfDay());
                    }
                    skip = true;
                }
            }
            case MINUTE: {
                if(skip) {
                    LocalTime lt = tradingSession.start();
                    dt = dt.withMinuteOfHour(lt.getMinuteOfHour());
                }else{
                    dt = dt.plusMinutes(interval);
                    tradingSession = getTradingSessionOnOrAfter(dt);
                    if(!tradingSession.contains(dt)) {
                        if(tradingSession.day() < dt.getDayOfWeek()) {
                            dt = dt.plusDays((DateTimeConstants.SUNDAY - dt.getDayOfWeek()) + 1);
                        }
                        dt = dt.withDayOfWeek(tradingSession.day());
                        LocalTime lt = tradingSession.start();
                        dt = dt.withHourOfDay(lt.getHourOfDay()).withMinuteOfHour(lt.getMinuteOfHour());
                    }
                    skip = true;
                }
            }
            case SECOND: {
                if(skip) {
                    dt = dt.withSecondOfMinute(0);
                }else{
                    dt = dt.plusSeconds(interval);
                    tradingSession = getTradingSessionOnOrAfter(dt);
                    if(!tradingSession.contains(dt)) {
                        if(tradingSession.day() < dt.getDayOfWeek()) {
                            dt = dt.plusDays((DateTimeConstants.SUNDAY - dt.getDayOfWeek()) + 1);
                        }
                        dt = dt.withDayOfWeek(tradingSession.day());
                        LocalTime lt = tradingSession.start();
                        dt = dt.withHourOfDay(lt.getHourOfDay()).withMinuteOfHour(lt.getMinuteOfHour());
                        dt = dt.withSecondOfMinute(0);
                    }
                }
                break;
            }
            case TICK: {
            	//Figure this out
            }
            
            // Guarantee new date is aligned to a business date.
            calculator.setStartDate(dt.toLocalDate());
            LocalDate ldt = calculator.getCurrentBusinessDate();
            if(!ldt.isEqual(dt.toLocalDate())) {
                tradingSession = getTradingSessionOnOrAfter(ldt.toDateTime(dt.toLocalTime()));
                if(tradingSession.day() < dt.getDayOfWeek()) {
                    dt = dt.plusDays((DateTimeConstants.SUNDAY - dt.getDayOfWeek()) + 1);
                }
                dt = dt.withDayOfWeek(tradingSession.day());
                LocalTime lt = tradingSession.start();
                dt = dt.withHourOfDay(lt.getHourOfDay()).withMinuteOfHour(lt.getMinuteOfHour()).withSecondOfMinute(0);
            }
        }
        return dt;
    }
    
    /**
     * Returns a default {@link TradingWeek} used for testing.
     * 
     * @return  a default {@link TradingWeek}.
     */
    public static TradingWeek loadDefaultTradingWeek() {
        int[] days = new int[] { 
            DateTimeConstants.SUNDAY,
            DateTimeConstants.MONDAY,
            DateTimeConstants.TUESDAY,
            DateTimeConstants.WEDNESDAY,
            DateTimeConstants.THURSDAY,
            DateTimeConstants.FRIDAY,
            DateTimeConstants.SATURDAY
        };
        
        //Load the TradingWeek
        WorkingWeek ww = new WorkingWeek();
        for(int day : days) {
            ww = ww.withWorkingDayFromCalendar(true, day);
        }
         
        TradingWeek tradingWeek = new TradingWeek(ww.getWorkingDays());
        for(int i = 0;i < days.length;i++) {
            int day = days[i];
            tradingWeek.addTradingSession(new TradingSession(day, day,
                new LocalTime(8, 30, 0, 0), new LocalTime(15, 30, 0, 0)));
        }
        
        return tradingWeek;
    }
    
    /**
     * Returns a flag indicating whether the specified date is a proper
     * "trading day".
     */
    @Override
    public boolean isWorkingDay(LocalDate date) {
        int day = date.getDayOfWeek();
        for(TradingSession ts : sessions) {
            if(ts.day() == day) return true;
        }
        return false;
    }
    
    /**
     * Returns the number of real natural, (not session), days between the startDay and endDay specified.
     * 
     * @param startDay      the day of start - on of the {@link DateTimeConstants}
     * @param endDay        the day of end - on of the {@link DateTimeConstants}
     * @return  the number of days between the startDay and endDay specified.
     */
    public static int naturalDaysBetween(int startDay, int endDay) {
        if(endDay < startDay) {
            return (DateTimeConstants.SUNDAY - startDay) + endDay;
        }
        return endDay - startDay;
    }

    /**
     * Convenience method to return the day of week of the specified day 
     * specified as an int.
     * 
     * @return  this TradingSession's day in string form.
     * @see DateTimeConstants
     */
    public static String dayToString(int day) {
        switch(day) {
            case DateTimeConstants.SUNDAY: return "Sunday";
            case DateTimeConstants.MONDAY: return "Monday";
            case DateTimeConstants.TUESDAY: return "Tuesday";
            case DateTimeConstants.WEDNESDAY: return "Wednesday";
            case DateTimeConstants.THURSDAY: return "Thursday";
            case DateTimeConstants.FRIDAY: return "Friday";
            case DateTimeConstants.SATURDAY: return "Saturday";
            default: return null;
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sessions == null) ? 0 : sessions.hashCode());
        result = prime * result + (int) (weekMillis ^ (weekMillis >>> 32));
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
        TradingWeek other = (TradingWeek) obj;
        if (sessions == null) {
            if (other.sessions != null)
                return false;
        } else if (!equalSessions(other.sessions))
            return false;
        if (weekMillis != other.weekMillis)
            return false;
        return true;
    }
    
    private boolean equalSessions(List<TradingSession> l) {
        for(TradingSession ts : sessions) {
            for(TradingSession ts2 : l) {
                if(!ts.equals(ts2)) return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TradingWeek:\n");
        for(TradingSession ts : sessions) {
            sb.append("\t"+ts+"\n");
        }
        return sb.toString();
    }
    
    
    
    ////////////////////////////////////// Builder Class Definitions ////////////////////////////////////
    
    /**
     * Base class ascribing to the "Builder Pattern" for creating builders
     * capable of creating {@code TradingWeek}s.
     */
    public static abstract class Builder {
        int[] days;
        List<TradingSession> sessions = new ArrayList<TradingSession>();
        String holidayCalendarName;
        HolidayCalendar<LocalDate> calendar;
        
        
        /**
         * Instantiates a new DefaultBuilder object.
         */
        private Builder() {}
        
        /**
         * Supplies this builder with an array of integers representing days desired
         * in the constructed {@link TradingWeek}. The supplied integers should be
         * consistent with days of the week as defined by {@link DateTimeConstants}.
         * 
         * @param daysFromDateTimeConstants     array of integers representing days
         *                                      in the constructed TradingWeek.
         * @throws  IllegalArgumentException    when the array passed in is null, zero length
         *                                      or > 7. 
         */
        public void setTradingDays(int[] daysFromDateTimeConstants) {
            if(daysFromDateTimeConstants == null || daysFromDateTimeConstants.length < 1) {
                throw new IllegalArgumentException("Attempt to create Builder with zero working days.");
            }else if(daysFromDateTimeConstants.length > 7) {
                throw new IllegalArgumentException("There can only be a maximum of 7 days in a trading week");
            }
            this.days = daysFromDateTimeConstants;
        }
        
        /**
         * Adds a configured session to be added to the constructed {@link TradingWeek}.
         * Start and end times must be ":" delimited and consist of four fields - that
         * is hours:minutes:seconds:milliseconds for example: "03:30:00:00". The beginDay.
         * and endDay parameters should be consistent with {@link org.joda.time.DateTimeConstants},
         * where Monday == 1, and Sunday == 7.
         * 
         * @param beginDay      the starting day of the constructed {@link TradingWeek}.
         * @param startTime     the starting time of on the start day of the session.
         * @param endDay        the ending day of the constructed {@link TradingWeek}.
         * @param endTime       the ending time of day on the ending day of the session.
         */
        public void addTradingSession(int beginDay, String startTime, int endDay, String endTime) {
            String[] start = startTime.split("\\:");
            String[] end = endTime.split("\\:");
            if(start.length < 4 || start.length > 4) {
                throw new IllegalArgumentException("Start time must be a colon delimitted set of numbers of the form h:m:s:ms - "+
                    dayToString(beginDay)+"-"+startTime+", "+dayToString(endDay)+"-"+endTime);
            }
            int[] startInts = new int[] { 
                Integer.parseInt(start[0].trim()), Integer.parseInt(start[1].trim()),
                    Integer.parseInt(start[2].trim()), Integer.parseInt(start[3].trim()) };
            int[] endInts = new int[] { 
                Integer.parseInt(end[0].trim()), Integer.parseInt(end[1].trim()),
                    Integer.parseInt(end[2].trim()), Integer.parseInt(end[3].trim()) };
            TradingSession session = new TradingSession(beginDay, endDay,
                new LocalTime(startInts[0], startInts[1], startInts[2], startInts[3]), 
                    new LocalTime(endInts[0], endInts[1], endInts[2], endInts[3]));
            
            sessions.add(session);
        }
        
        /**
         * A Calendar name must be specified. All calendars may be shared by the <em>unique</em> 
         * name its given, therefore a check is first made to see if a calendar already exists
         * with the name specified, and is used in the case where it has already been loaded. 
         * This is to save time during loading.
         * 
         * @param holidayCalendarName       the name of the holiday calendar - used for deferred loading
         *                                  where the calendar specified has already been loaded. Calendar
         *                                  names must be <em>unique</em>. 
         */
        public void setHolidayCalendarName(String holidayCalendarName) {
            this.holidayCalendarName = holidayCalendarName;
        }
        
        /**
         * Builds a new immutable {@link TradingWeek} using the "builder pattern" to ensure
         * correct and whole configuration.
         *  
         * @return  a correctly configured {@code TradingWeek}.
         * @throws IllegalStateException    if there are problems loading a holiday calendar,
         *                                  from the parameters specified, or there are no 
         *                                  {@link TradingSession}s configured.
         */
        public TradingWeek build() throws IllegalStateException {
            calendar = LocalDateKitCalculatorsFactory.getDefaultInstance().getHolidayCalendar(holidayCalendarName);
            if(calendar == null) {
                try {
                    calendar = loadHolidayCalendar();
                    
                    // register the holiday calendar (any calculator with name "CALENDAR_NAME" asked from now on 
                    // will receive an IMMUTABLE reference to this calendar)
                    LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays(holidayCalendarName, calendar);
                }catch(Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            
            // ask for a LocalDate calculator for "CALENDAR_NAME" 
            //(even if a new set of holidays is registered, this one calculator is not affected)
            DateCalculator<LocalDate> calculator = LocalDateKitCalculatorsFactory.getDefaultInstance().
                getDateCalculator(holidayCalendarName, HolidayHandlerType.FORWARD);
            
            try {
                loadTradingDaysAndSessions();
                if(sessions.size() < 1) {
                    throw new IllegalStateException("Cannot build a TradingWeek with zero TradingSessions.");
                }
            }catch(IOException io) {
                throw new IllegalStateException(io);
            }
            
            TradingWeek rawTradingWeek = createRawTradingWeek();
            for(TradingSession session : sessions) {
                rawTradingWeek.addTradingSession(session);
            }
            
            calculator.setWorkingWeek(rawTradingWeek);
            rawTradingWeek.calculator = calculator;
            System.out.println("internal isTradingDay ? " + !calculator.isNonWorkingDay(new DateTime("1610-1-1").toLocalDate()));
            return rawTradingWeek;
        }
        
        protected abstract HolidayCalendar<LocalDate> loadHolidayCalendar() throws IOException;
        
        protected abstract void loadTradingDaysAndSessions() throws IOException;
        
        /**
         * Instantiates a "raw" {@link TradingWeek}, that is, a TradingWeek
         * without any holidays yet configured.
         * 
         * @return  a "raw" {@code TradingWeek}.
         */
        private TradingWeek createRawTradingWeek() {
            //Load the TradingWeek
            WorkingWeek ww = new WorkingWeek();
            for(int day : days) {
                ww = ww.withWorkingDayFromCalendar(true, day);
            }
             
            TradingWeek tradingWeek = new TradingWeek(ww.getWorkingDays());            
            return tradingWeek;
        }
    }
    
    /**
     * Ascribes to the "builder pattern" to create a builder capable of returning
     * {@code TradingWeek}s configured from {@code Properties} file paths on the
     * current classpath.
     * 
     * @author David Ray
     */
    public static class ConfigBuilder extends Builder {
        private Properties properties;
        private String holidayDateFileLocation;
        private String holidayDelimiter;
        private String sessionParameterDelimiter;
        private String sessionDelimiter;
        private String tradingWeekName;
        private String calendarName;
        
        private static final String SESSION_KEY = "sessions";
        private static final String HOLIDAY_FILEPATH_KEY = "holidayDateFileLocation";
        private static final String HOLDIAY_FILE_DELIMITER = "holidayDelimiter";
        private static final String SESSION_PARAM_DELIMITER = "sessionParamDelimiter";
        private static final String SESSION_DELIMITER = "sessionDelimiter";
        private static final String HOLIDAY_FILE_LOADTYPE = "holidayDateFileLoadType";
        
       
        
        /**
         * Constructs a new {@code ConfigBuilder} which can properly construct a 
         * new {@link TradingWeek}
         *  
         * @param propertiesFilePath
         * @param calendarName
         * @param tradingWeekName
         * @throws IOException
         * @throws IllegalArgumentException
         */
        private ConfigBuilder(String propertiesFilePath, String calendarName, String tradingWeekName) 
            throws IOException, IllegalArgumentException {
           
            this(loadProperties(propertiesFilePath, calendarName), calendarName, tradingWeekName);
        }
        
        /**
         * Constructs a new {@code ConfigBuilder} which can properly construct a 
         * new {@link TradingWeek}
         * 
         * @param propertiesObj
         * @param calendarName
         * @param tradingWeekName
         * @throws IOException
         * @throws IllegalArgumentException
         */
        private ConfigBuilder(Properties propertiesObj, String calendarName, String tradingWeekName) 
            throws IOException, IllegalArgumentException {
            
            super();
            properties = propertiesObj;
            if(calendarName == null || calendarName.length() < 1) {
                throw new IllegalArgumentException("Calendar name cannot be null");
            }else if(tradingWeekName == null || tradingWeekName.length() < 1){
                throw new IllegalArgumentException("Session name cannot be null");
            }
          
            this.tradingWeekName = tradingWeekName;
            this.calendarName = calendarName;
        }
        
        /**
         * Returns a properly constructed {@link TradingWeek}
         */
        @Override
        public TradingWeek build() throws IllegalStateException {
            validateAndSetCalendarParams(properties, calendarName);
            validateAndSetSessionParamDelimiters(properties, tradingWeekName);
            return super.build();
        }
        
        /**
         * Returns a {@link Properties} object containing 
         * trading session information.
         * 
         * @param propertiesFilePath
         * @return
         * @throws IOException
         * @throws IllegalArgumentException
         */
        private static Properties loadProperties(String propertiesFilePath, String calendarName) 
            throws IOException, IllegalArgumentException {
            Properties properties = new Properties();
            try {
                properties.load(new InputStreamReader(ConfigBuilder.class.getClassLoader().getResourceAsStream(propertiesFilePath)));
            }catch(Exception io) {
                throw new IOException("hint: make sure properties file \""+propertiesFilePath+"\" is on the classpath", io);
            }
            properties.put(calendarName + "." + ConfigBuilder.HOLIDAY_FILE_LOADTYPE , LoadType.MEMORY);
            return properties;
        }
            
        /**
         * Loads holiday information from a resource path on the declared classpath.
         * 
         * @return  a new instance of {@link HolidayCalendar}.
         * @throws IOException   thrown if there is a non-existent file, the path
         *                       to the holiday file is incorrect, or there were
         *                       problems parsing the specified file.
         */
        @Override
        protected HolidayCalendar<LocalDate> loadHolidayCalendar() throws IOException {
            if(holidayCalendarName == null) {
                throw new NullPointerException("No holiday calendar name specified.");
            }
            
            List<LocalDate> holidayDates = new ArrayList<LocalDate>();
            
            BufferedReader buf = null;
            if(properties.get(holidayCalendarName +"."+HOLIDAY_FILE_LOADTYPE) == LoadType.MEMORY) {
            	String holidayString = properties.getProperty(holidayCalendarName + "." + "holidayDates","1610-1-1");
            	buf = new BufferedReader(new StringReader(holidayString));
            }else{
                buf = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream(holidayDateFileLocation)));
            }
            
            String dateString = null;
            if(holidayDelimiter == null) {
                while((dateString = buf.readLine()) != null) {
                    dateString = dateString.trim();
                    holidayDates.add(new LocalDate(dateString));
                }
            }else{
            	dateString = buf.readLine().trim();
                String[] dateStringArray = dateString.split("\\" + holidayDelimiter);
                List<String> dateList = Arrays.asList(dateStringArray);
                for(String s : dateList) {
                    holidayDates.add(new LocalDate(s));
                }
            }
            
            // create the HolidayCalendar
            LocalDate startDate = holidayDates.get(0);
            LocalDate endDate = holidayDates.get(holidayDates.size() - 1);
            HolidayCalendar<LocalDate> calendar = new DefaultHolidayCalendar<LocalDate>(
                new HashSet<LocalDate>(holidayDates), startDate.minusYears(100), endDate.plusYears(1000));
              
            return calendar;
        }
        
        /**
         * Loads the trading sessions from a property key/value pair in the
         * configured {@link Properties} file.
         * <p>
         * Each {@link TradingSession} has a statement of sessions identified with it.
         * The key used is the session name plus a period then the word "sessions", as in
         * TEST.sessions=<...>, where "TEST" is the trading week name and "sessions" is the
         * second part of the key identifying the session statement. 
         * <p>
         * The value part (the part following the equals sign), is described fully here...
         * {@link #setSessionParameterDelimiter(String)} 
         */
        @Override
        protected void loadTradingDaysAndSessions() {
            String tradingSessionsValue = properties.getProperty(tradingWeekName + "." + SESSION_KEY, null);
            if(tradingSessionsValue == null) {
                throw new IllegalArgumentException("No trading sessions specified in config file.");
            }
            
            Set<Integer> tradingDays = new LinkedHashSet<Integer>();
            String[] tradingSessions = tradingSessionsValue.trim().split("\\"+sessionDelimiter);
            int[] retVal = new int[tradingSessions.length];
            for(int i = 0;i < retVal.length;i++) {
                String[] sessionArray = tradingSessions[i].trim().split("\\"+sessionParameterDelimiter);
                
                Integer beginDay = new Integer(Integer.parseInt(sessionArray[0].trim()));
                Integer endDay = new Integer(Integer.parseInt(sessionArray[2].trim()));
                if(!tradingDays.contains(beginDay)) {
                    tradingDays.add(beginDay);
                }
                if(!tradingDays.contains(endDay)) {
                    tradingDays.add(endDay);
                }
                              
                addTradingSession(beginDay.intValue(), sessionArray[1].trim(), endDay.intValue(), sessionArray[3].trim());
            }
            
            int[] days = new int[tradingDays.size()];
            int i = 0;
            for(Integer I : tradingDays) {
                days[i++] = I.intValue();
            }
            setTradingDays(days);
        }
        
        /**
         * Sets the String identifying the file location such as /MyDirectory/holidayDates.txt - 
         * the file extension can be any extension or non-existent. If the dates specified are each on 
         * a single line, a sessionParameterDelimiter may be specified though the sessionDelimiter must be null where 
         * dates exist on different lines.
         * 
         * This class uses the class loader to locate all resources, which means the path specified
         * must be on the classpath.
         * 
         * The dates specified are expected to be of the form, "2010-12-25", that is year-month-day.
         * 
         * @param filePath                  the path to the file containing holiday dates. All paths
         *                                  must be relative to the root of the classpath
         * @param holidayDelimiter          an (optional - may be null) delimiter in the case where dates
         *                                  in the file specified are listed on a single line.
         */
        public void setHolidayDateFileLocation(String filePath, String holidayDelimiter) {
            this.holidayDateFileLocation = filePath;
            this.holidayDelimiter = holidayDelimiter;
        }
        
        /**
         * Sets the delimiter separating each session statement in the value portion of 
         * a given properties file as in..
         * <p>
         * (TEST3.sessions=2,08:30:0:0,2,15:30:0:0;3,08:30:0:0,3,15:30:0:0;4,08:30:0:0,4,15:30:0:0. 
         * <p>
         * A session takes the form of "7,08:30:0:0,7,15:30:0:0;", where 7 is the DateTimeConstant
         * for Sunday, 08:30:0:0 is the begin time of the session, 7 again
         * is the DateTimeConstant specifying the end day (Sunday again),
         * 15:30:0:0 is the end time - the ";" at the end of the String is the sessionDelimiter,
         * the ","'s are the sessionParameterDelimiter(s).
         * 
         * @param sessionParamDelimiter     the delimiter separating each session. A session takes the 
         *                                  form of "7,08:30:0:0,7,15:30:0:0;", where 7 is the DateTimeConstant
         *                                  for Sunday, 08:30:0:0 is the begin time of the session, 7 again
         *                                  is the DateTimeConstant specifying the end day (Sunday again),
         *                                  15:30:0:0 is the end time - the ";" at the end of the String is the sessionDelimiter,
         *                                  the ","'s are the sessionParameterDelimiter(s).
         * @see org.joda.time.DateTimeConstants
         */
        public void setSessionParameterDelimiter(String sessionParamDelimiter) {
            this.sessionParameterDelimiter = sessionParamDelimiter;
        }
        
        /**
         * Sets the session delimiter.
         * <p>
         * For more information see {@link #setSessionParameterDelimiter(String)}
         * @param sessionDelimiter
         * @see #setSessionParameterDelimiter(String)
         */
        public void setSessionDelimiter(String sessionDelimiter) {
            this.sessionDelimiter = sessionDelimiter;
        }
        
        /**
         * Validates that the declared calendar parameters necessary for finding and
         * parsing the holiday file have been set.
         * 
         * @param properties        the {@link Properties} object containing location information
         *                          for the holiday file.
         * @param calendarName      the calendar whose location and delimiters are being declared.
         */
        private void validateAndSetCalendarParams(Properties properties, String calendarName) {
            String filePath = properties.getProperty(calendarName + "." + HOLIDAY_FILEPATH_KEY, null);
            if(filePath == null && properties.get(calendarName + "." + HOLIDAY_FILE_LOADTYPE) != LoadType.MEMORY) {
                throw new IllegalArgumentException("Could not find the holiday file path in config file for calendar: "+calendarName);
            }
            
            String delimiter = properties.getProperty(calendarName + "." + HOLDIAY_FILE_DELIMITER, null);
            setHolidayDateFileLocation(filePath, delimiter);
            setHolidayCalendarName(calendarName);
        }
        
        /**
         * Validates that the declared delimiters necessary for parsing the session 
         * properties, have been set.
         * 
         * @param properties        the {@link Properties} object containing session parameters.
         * @param sessionName       the name of the Session, which proceeds each property so that
         *                          a single properties object may specify session parameters for 
         *                          more than one session.
         */
        private void validateAndSetSessionParamDelimiters(Properties properties, String sessionName) {
            String sessionParamDelimiter = properties.getProperty(sessionName + "." + SESSION_PARAM_DELIMITER, null);
            if(sessionParamDelimiter == null) {
                throw new IllegalArgumentException("Could not find any session parameter delimiter for session: "+sessionName);
            }
            setSessionParameterDelimiter(sessionParamDelimiter);
            
            String sessionDelimiter = properties.getProperty(sessionName + "." + SESSION_DELIMITER, null);
            if(sessionDelimiter == null) {
                throw new IllegalArgumentException("Could not find any session delimiter for session: "+sessionName);
            }
            setSessionDelimiter(sessionDelimiter);
        }
        
       
    }
}
