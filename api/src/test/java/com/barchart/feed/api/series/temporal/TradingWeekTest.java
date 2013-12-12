package com.barchart.feed.api.series.temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.joda.time.DateTime;
import org.junit.Test;

public class TradingWeekTest {

    private enum WeekType { 
        DEFAULT("chart.testprops"); 
        
        private String path;
        
        private WeekType(String path) {
            this.path = path;
        }
        
        public String path() {
            return path;
        }
    };
    
    private final String TEST = "TEST";
    private final String TEST3 = "TEST3";
    
    /**
     * Test that we can get the correct session containing a given DateTime.
     */
    @Test
    public void testGetTradingSessionOnOrAfter() {
        TradingWeek tradingWeek = getTestTradingWeek(WeekType.DEFAULT, TEST);
        
        assertEquals(7, tradingWeek.length());
        
        //FIRST: Test within bounds Sunday, May 3 - Saturday, May 9
        DateTime testDate = new DateTime(2009, 5, 3, 8, 30, 0, 0);
        TradingSession session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Sunday(08:30:00.000-15:30:00.000)", session.toString());
        
        testDate = new DateTime(2009, 5, 4, 8, 30, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Monday(08:30:00.000-15:30:00.000)", session.toString());
        
        testDate = new DateTime(2009, 5, 5, 8, 30, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Tuesday(08:30:00.000-15:30:00.000)", session.toString());
        
        testDate = new DateTime(2009, 5, 6, 8, 30, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Wednesday(08:30:00.000-15:30:00.000)", session.toString());
        
        testDate = new DateTime(2009, 5, 7, 8, 30, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Thursday(08:30:00.000-15:30:00.000)", session.toString());
        
        testDate = new DateTime(2009, 5, 8, 8, 30, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Friday(08:30:00.000-15:30:00.000)", session.toString());
        
        testDate = new DateTime(2009, 5, 9, 8, 30, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Saturday(08:30:00.000-15:30:00.000)", session.toString());
        
        //////////////////////////////////////////////////////////
        //SECOND: Test advancement to next session
        //Advancement due to hours
        testDate = new DateTime(2009, 5, 2, 16, 30, 0, 0); //Saturday, May 2nd
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Sunday(08:30:00.000-15:30:00.000)", session.toString());
        
        //Advancement due to minutes
        testDate = new DateTime(2009, 5, 2, 15, 31, 0, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Sunday(08:30:00.000-15:30:00.000)", session.toString());
        
        //Advancement due to seconds
        testDate = new DateTime(2009, 5, 2, 15, 30, 1, 0);
        session = tradingWeek.getTradingSessionOnOrAfter(testDate);
        assertEquals("Sunday(08:30:00.000-15:30:00.000)", session.toString());
    }
    
    /**
     * Test that the DateTime input is advanced correctly for each
     * PeriodType.
     */
    @Test
    public void testGetNextSessionDate() {
        TradingWeek tradingWeek = getTestTradingWeek(WeekType.DEFAULT, TEST);
        
        DateTime testDate = new DateTime(2009, 5, 1, 0, 0, 0, 999);
        
        Period tu = new Period(PeriodType.YEAR, 1);
        DateTime nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2010-01-01T08:30:00.000-06:00", nextDate.toString());
        
        tu = new Period(PeriodType.QUARTER, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-07-01T08:30:00.000-05:00", nextDate.toString());
        
        tu = new Period(PeriodType.MONTH, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-06-01T08:30:00.000-05:00", nextDate.toString());
        
        tu = new Period(PeriodType.WEEK, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-05-10T08:30:00.000-05:00", nextDate.toString());
        
        tu = new Period(PeriodType.DAY, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-05-02T08:30:00.000-05:00", nextDate.toString());
        
        testDate = new DateTime(2009, 5, 1, 8, 30, 0, 999);
        tu = new Period(PeriodType.HOUR, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-05-01T09:30:00.000-05:00", nextDate.toString());
        
        tu = new Period(PeriodType.MINUTE, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-05-01T08:31:00.000-05:00", nextDate.toString());
        
        tu = new Period(PeriodType.SECOND, 1);
        nextDate = tradingWeek.getNextSessionDate(testDate, tu);
        assertEquals("2009-05-01T08:30:01.000-05:00", nextDate.toString());
    }
    
    @Test
    public void testGetMillisBetween() {
        TradingWeek week = getTestTradingWeek(WeekType.DEFAULT, TEST);
        
        //Test comparison convenience variables
        long millisInHour = 3600000;
        long sessionMillis = millisInHour * 7;
        long weekMillis = sessionMillis * 7;
        
        //////////////////// Test within a single session
        DateTime dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        DateTime dt2 = new DateTime(2009, 5, 1, 9, 30, 0, 0);
        long millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(millisInHour, millis);
        
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 1, 15, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(millisInHour * 7, millis);
        
        //////////////////// Test dates spanning more than 1 session
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 3, 8, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(millisInHour * 14, millis);
        
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 3, 13, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(millisInHour * 19, millis);
        
        //////////////////// Test dates spanning a week or more
        //Test 1 week
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 8, 8, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(weekMillis, millis);
        
        //Test 2 weeks
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 15, 8, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(weekMillis * 2, millis);
        
        //Test 4 weeks
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 29, 8, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        assertEquals(weekMillis * 4, millis);
        
        //Test 4 weeks and 1 day
        dt1 = new DateTime(2009, 5, 1, 8, 30, 0, 0);
        dt2 = new DateTime(2009, 5, 30, 8, 30, 0, 0);
        millis = week.getSessionMillisBetween(dt1, dt2);
        long millisInSession = week.getTradingSessionOnOrAfter(dt2).sessionMillis();
        assertEquals((weekMillis * 4) + millisInSession, millis);
        
    }
    
    @Test
    public void testGetTradingDaysInMonth() {
        TradingWeek week = getTestTradingWeek(WeekType.DEFAULT, TEST3);
        DateTime dt1 = new DateTime(2009, 10, 11, 8, 30, 0, 0);
        assertEquals(13, week.getTradingDaysInMonth(dt1));
    }
    
    private static Properties DEFAULT_PROPS; 
    public static TradingWeek DEFAULT;
    private TradingWeek getTestTradingWeek(WeekType type, String symbol) {
        DEFAULT_PROPS = new Properties();
        DEFAULT_PROPS.put("DEFAULT.holidayDateFileLoadType", TradingWeek.LoadType.MEMORY);
        DEFAULT_PROPS.put("DEFAULT.holidayDelimiter", ",");
        DEFAULT_PROPS.put("DEFAULT.holidayDates", "2013-1-1");
        
        DEFAULT_PROPS.setProperty("TEST.sessionParamDelimiter", ",");
        DEFAULT_PROPS.setProperty("TEST.sessionDelimiter", ";");
        DEFAULT_PROPS.setProperty("TEST.sessions", "7,08:30:0:0,7,15:30:0:0;1,08:30:0:0,1,15:30:0:0;2,08:30:0:0,2,15:30:0:0;3,08:30:0:0,3,15:30:0:0;4,08:30:0:0,4,15:30:0:0;5,08:30:0:0,5,15:30:0:0;6,08:30:0:0,6,15:30:0:0");
        
        DEFAULT_PROPS.setProperty("TEST3.sessionParamDelimiter", ",");
        DEFAULT_PROPS.setProperty("TEST3.sessionDelimiter", ";");
        DEFAULT_PROPS.setProperty("TEST3.sessions", "2,08:30:0:0,2,15:30:0:0;3,08:30:0:0,3,15:30:0:0;4,08:30:0:0,4,15:30:0:0");
        
        try {
            DEFAULT = TradingWeek.configBuilder(DEFAULT_PROPS, "DEFAULT", symbol).build();
        } catch(Exception e) { throw new IllegalStateException("could not initialize default trading session."); }
        
        return DEFAULT;
    }
    

}
