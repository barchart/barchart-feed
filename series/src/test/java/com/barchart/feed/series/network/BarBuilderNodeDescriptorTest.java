package com.barchart.feed.series.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;
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
    public void testInstantiateBuilderAnalytic() {
        String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf1 = new TimeFrameImpl(new Period(PeriodType.TICK, 1), dt1, null);
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrameImpl[] { tf1 }, TradingWeekImpl.DEFAULT);
        
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
