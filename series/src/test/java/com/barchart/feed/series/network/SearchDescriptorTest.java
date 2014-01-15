package com.barchart.feed.series.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;
import com.barchart.feed.series.network.AnalyticNodeDescriptor;
import com.barchart.feed.series.network.NetworkSchema;
import com.barchart.feed.series.network.SeriesSubscription;
import com.barchart.feed.series.network.BarchartSeriesProvider.SearchDescriptor;

public class SearchDescriptorTest {

    @Test
    public void testSearchDescriptorEquality() {
        String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf1 = new TimeFrameImpl(new Period(PeriodType.TICK, 1), dt1, null);
        
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrameImpl[] { tf1 }, TradingWeekImpl.DEFAULT);
        NetworkSchema.setSchemaFilePath("networks.txt");
        NetworkSchema.reloadDefinitions();
        AnalyticNodeDescriptor desc = NetworkSchema.lookup("PP_S3L", 2);
        
        SearchDescriptor searchKey = new SearchDescriptor(sub1, desc);
        
        assertNotNull(desc);
        assertNotNull(searchKey);
        
        //Detect sameness with 2 different instances with same params
        SearchDescriptor searchKey2 = new SearchDescriptor(sub1, desc);
        assertEquals(searchKey, searchKey2);
        
        AnalyticNodeDescriptor unEqDesc = NetworkSchema.lookup("PP_S3", 1);
        assertNotNull(unEqDesc);
        
        //Detect difference based on node descriptor change
        SearchDescriptor searchKey3 = new SearchDescriptor(sub1, unEqDesc);
        assertNotEquals(searchKey, searchKey3);
        
        //Detect difference based on slight start date change
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 1, 0);
        TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.TICK, 1), dt2, null);
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);
        SearchDescriptor searchKey4 = new SearchDescriptor(sub2, desc);
        assertNotEquals(searchKey, searchKey4);
    }

}
