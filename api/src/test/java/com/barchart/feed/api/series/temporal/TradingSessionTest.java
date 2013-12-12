package com.barchart.feed.api.series.temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.junit.Test;

public class TradingSessionTest {

    @Test
    public void testContains() {
        TradingSession session = new TradingSession(DateTimeConstants.FRIDAY, DateTimeConstants.FRIDAY, new LocalTime(8, 30), new LocalTime(15, 30));
        DateTime test = new DateTime(2010, 9, 10, 8, 30, 0, 0);
        assertTrue(session.contains(test));
        
        test = new DateTime(2010, 9, 10, 15, 30, 0, 0);
        assertTrue(session.contains(test));
        
        test = new DateTime(2010, 9, 10, 8, 29, 0, 0);
        assertFalse(session.contains(test));
        
        test = new DateTime(2010, 9, 10, 15, 31, 0, 0);
        assertFalse(session.contains(test));
        
        test = new DateTime(2010, 9, 11, 8, 30, 0, 0);
        assertFalse(session.contains(test));
        
        //Test corner case where the TradingSession spans two days
        session = new TradingSession(DateTimeConstants.SUNDAY, DateTimeConstants.TUESDAY, new LocalTime(18, 30), new LocalTime(3, 30));
        test = new DateTime(2010, 9, 13, 3, 29, 59, 999);// 9-13 is a Monday
        assertTrue(session.contains(test));
        
        session = new TradingSession(DateTimeConstants.SUNDAY, DateTimeConstants.TUESDAY, new LocalTime(18, 30), new LocalTime(3, 30));
        test = new DateTime(2010, 9, 11, 3, 29, 59, 999);// 9-11 is a Saturday
        assertFalse(session.contains(test));
        
        session = new TradingSession(DateTimeConstants.SUNDAY, DateTimeConstants.TUESDAY, new LocalTime(18, 30), new LocalTime(3, 30));
        test = new DateTime(2010, 9, 13, 3, 30, 01, 999);// 9-13 is a Monday
        assertTrue(session.contains(test));
    }
    
    @Test
    public void testSessionMillis() {
        long millisInHour = 3600000;
        long millisInDay = millisInHour * 24;
        
        DateTime dt = new DateTime(2010, 9, 13, 0, 0, 0, 0);  //Monday
        DateTime dt2 = new DateTime(2010, 9, 15, 0, 0, 0, 0); //Wednesday
        
        long sessionMillis = TradingSession.sessionMillis(
                dt.dayOfWeek().get(), dt.toLocalTime(), dt2.dayOfWeek().get(), dt2.toLocalTime());
        assertEquals(millisInDay * 2, sessionMillis);
        
        //Test rollover across Sunday boundary
        dt = new DateTime(2010, 9, 12, 0, 0, 0, 0);     //Sunday
        dt2 = new DateTime(2010, 9, 14, 0, 0, 0, 0);    //Tuesday
        
        sessionMillis = TradingSession.sessionMillis(
                dt.dayOfWeek().get(), dt.toLocalTime(), dt2.dayOfWeek().get(), dt2.toLocalTime());
        assertEquals(millisInDay * 2, sessionMillis);
    }

}
