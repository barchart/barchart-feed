package com.barchart.feed.series.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.series.TimeFrame;
import com.barchart.feed.series.TradingWeek;

public class SubscriptionTest {

    /**
     * We need to test:
     * 1. Symbols are same
     * 2. TimeFrames: (assuming sub1 is the "source" and sub2 is the candidate "sink")
     *      a. StartDates for sub2 are equal or sub2 is after sub1
     *      b. For all TimeFrames:
     *          I. sub1.PeriodType is lower than sub2.PeriodType
     *          II. sub1.PeriodType is lower than PeriodType.WEEK (one of [TICK, MINUTE, HOUR, DAY] )
     *          III. sub1.Period.Interval == 1
     *          --OR--
     *          I. sub1.PeriodType is equal than sub2.PeriodType
     *          II. sub1.Period.Interval == 1
     *      c. Sessions (TradingWeek(s)) are the same.
     */ 
    @Test
    public void testIsDerivableFrom() {
        String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.TICK, 1), dt1, null);
        
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        symbol = "ESZ13";
        instr = TestHarness.makeInstrument(symbol);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.SECOND, 1), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        assertTrue(sub2.isDerivableFrom(sub1));
        
        //-----
        
        symbol = "ESZ13";
        instr = TestHarness.makeInstrument(symbol);
        DateTime dt3 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf3 = new TimeFrame(new Period(PeriodType.SECOND, 1), dt3, null);
        
        SeriesSubscription sub3 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf3 }, TradingWeek.DEFAULT);
        
        List<SeriesSubscription> subList = new ArrayList<SeriesSubscription>();
        subList.add(sub2);
        subList.add(sub1);
        
        assertTrue(subList.contains(sub3));
    }
        
    
}
