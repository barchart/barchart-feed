package com.barchart.feed.series.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.service.AnalyticNode;
import com.barchart.feed.series.service.BarBuilderNodeDescriptor;
import com.barchart.feed.series.service.SeriesSubscription;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Time;

public class AnalyticNodeTest {
    HashMap<String, SpanImpl> m = new HashMap<String, SpanImpl>();
    
    @Test
    public void testUpdateModifiedSpan_hasAllAncestorUpdates() {
        String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.TICK, 1), dt1, null);
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        symbol = "ESZ13";
        instr = TestHarness.makeInstrument(symbol);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MONTH, 7), dt2, null);
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        Analytic empty = TestHarness.makeEmptyAnalytic();
        
        AnalyticNode testNode = new AnalyticNode(empty);
        testNode.addInputKeyMapping("key1", sub1);
        testNode.addInputKeyMapping("key2", sub2);
        assertFalse(testNode.hasAllAncestorUpdates());  //No updates yet 
        
        Time t = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 12, 0, 0).getMillis());
        Time t2 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 13, 0, 0).getMillis());
        SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);
        testNode.updateModifiedSpan(newSpan, sub1);
        testNode.updateModifiedSpan(newSpan, sub2);
        assertTrue(testNode.hasAllAncestorUpdates());   //Has been updated
        
        testNode.process();                             //Processing resets modification state to unmodified
        
        assertFalse(testNode.hasAllAncestorUpdates());  //No new updates since above reset
        
        newSpan.setNextTime(ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 22, 0, 0).getMillis()));
        testNode.updateModifiedSpan(newSpan, sub1);
        testNode.updateModifiedSpan(newSpan, sub2);
        assertTrue(testNode.hasAllAncestorUpdates());   //New date extends processing time of last
    }
}
