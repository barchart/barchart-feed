package com.barchart.feed.series;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.series.TimeFrameImpl;

public class TimeFrameImplTest {

    @Test
    public void testIsDerivableFrom() {
        
        //////////////////////////////////////////////////////////
        ////   First test derivable Periods (no date checks)  ////
        //////////////////////////////////////////////////////////
        
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf1 = new TimeFrameImpl(new Period(PeriodType.TICK, 1), dt1, null);
        
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.SECOND, 1), dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.SECOND, 1), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.ONE_MINUTE, dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 1), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.ONE_HOUR, dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(Period.ONE_HOUR, dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.DAY, dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(Period.DAY, dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.WEEK, dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        //Above pattern breaks down here - nothing can be derived from weeks
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(Period.WEEK, dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.MONTH, dt2, null);
        
        assertFalse(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(Period.MONTH, dt1, null);
        
        //Pattern returns as viable her
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.QUARTER, dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(Period.QUARTER, dt1, null);
        
        //Pattern returns as viable her
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(Period.YEAR, dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        assertFalse(tf1.isDerivableFrom(tf2));
        
        //////////////////////////////////////////////////////////
        ////               Test Derivable Durations           ////
        //////////////////////////////////////////////////////////
        //5Min derivable from 1M
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(Period.ONE_MINUTE, dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        
        //5Min !Derivable from 5Min
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        
        //10Min Derivable from 5Min *IF* 2 % 1 == 0 and startDates are same and endDate1 == null or endDates are equal
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));
        
        //10Min Derivable from 5Min *IF* 2 % 1 == 0 and startDates are same and endDate1 == null or endDates are equal == (not due to 10 % 6)
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 6), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertFalse(tf2.isDerivableFrom(tf1));
        
        //10Min Derivable from 5Min *IF* 2 % 1 == 0 and startDates are same and endDate1 == null or endDates are equal == (not due to startDates !=)
        dt1 = new DateTime(2013, 12, 10, 11, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertFalse(tf2.isDerivableFrom(tf1));
        
        /////////////////// Test end date conditions ////////////////////
        //Derive source end date is not null and derive from is --> false
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt1, new DateTime(2013, 12, 20, 12, 0, 0));
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertFalse(tf2.isDerivableFrom(tf1));
        
        //Derive source end date is null and derive from is not --> true
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, new DateTime(2013, 12, 20, 12, 0, 0));
        
        assertTrue(tf2.isDerivableFrom(tf1));
        
        //End dates are equal --> true
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt1, new DateTime(2013, 12, 20, 12, 0, 0));
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, new DateTime(2013, 12, 20, 12, 0, 0));
        
        assertTrue(tf2.isDerivableFrom(tf1));
        
        
        //////////////////////////////////////////////////////////
        ////                Date boundary checks              ////
        //////////////////////////////////////////////////////////
        //Normal - isDerivable
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 1), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertTrue(tf2.isDerivableFrom(tf1));//new DateTime(2013, 12, 30, 12, 0, 0)
        
        //First frame's start after 2nd causes failure
        dt1 = new DateTime(2013, 12, 10, 13, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 1), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertFalse(tf2.isDerivableFrom(tf1));
        
        //2nd having end date, while first has no end date, does NOT cause failure 
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 1), dt1, null);
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, new DateTime(2013, 12, 30, 12, 0, 0));
        
        assertTrue(tf2.isDerivableFrom(tf1));
        
        //First having end date while 2nd not having end date causes failure
        dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf1 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 1), dt1, new DateTime(2013, 12, 30, 12, 0, 0));
        
        dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 10), dt2, null);
        
        assertFalse(tf2.isDerivableFrom(tf1));
        
        
    }

}
