package com.barchart.feed.series.service;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.feed.series.analytics.BarBuilder;

public class BarBuilderNodeDescriptorTest {
    
    @Test
    public void testGetLowerBaseType() {
        PeriodType type = BarBuilderNodeDescriptor.getLowerBaseType(PeriodType.MONTH);
        assertEquals(PeriodType.DAY, type);
        
        type = BarBuilderNodeDescriptor.getLowerBaseType(PeriodType.DAY);
        assertEquals(PeriodType.MINUTE, type);
        
        type = BarBuilderNodeDescriptor.getLowerBaseType(PeriodType.MINUTE);
        assertEquals(PeriodType.SECOND, type);
    }

    @Test
    public void testGetProcessorChain() {
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
        
        assertTrue(sub2.isDerivableFrom(sub1));
        
        BarBuilderNodeDescriptor nDesc = new BarBuilderNodeDescriptor();
        List<AnalyticNode> pList = nDesc.getNodeChain(sub1, sub2);
        int size = pList.size();
        assertEquals(5, size);
        assertEquals(new Period(PeriodType.MONTH, 7), 
            ((BarBuilderOld)pList.get(size - 1)).getOutputSubscription(null).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.MONTH, 1), 
            ((BarBuilderOld)pList.get(size - 2)).getOutputSubscription(null).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.DAY, 1), 
            ((BarBuilderOld)pList.get(size - 3)).getOutputSubscription(null).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.MINUTE, 1), 
            ((BarBuilderOld)pList.get(size - 4)).getOutputSubscription(null).getTimeFrames()[0].getPeriod());
        assertEquals(new Period(PeriodType.SECOND, 1), 
            ((BarBuilderOld)pList.get(size - 5)).getOutputSubscription(null).getTimeFrames()[0].getPeriod());
        
    }
    
    @Test
    public void testInstantiateBuilderAnalytic() {
        String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.TICK, 1), dt1, null);
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        BarBuilderNodeDescriptor desc = new BarBuilderNodeDescriptor();
        desc.setAnalyticClass(BarBuilder.class);
        desc.setConstructorArg(sub1);
        try {
            BarBuilder bb = (BarBuilder)desc.instantiateBuilderAnalytic();
            assertNotNull(bb);
        }catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
