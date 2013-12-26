package com.barchart.feed.series.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Ignore;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.service.BarBuilderNodeDescriptor;
import com.barchart.feed.series.service.BarBuilderOld;
import com.barchart.feed.series.service.SeriesSubscription;
import com.barchart.util.value.ValueFactoryImpl;

public class BarBuilderOldTest {

	@Ignore
	public void test() {
		String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        BarBuilderNodeDescriptor nDesc = new BarBuilderNodeDescriptor();
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.MINUTE, 1), dt1, null);
        
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, nDesc, new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        symbol = "ESZ13";
        instr = TestHarness.makeInstrument(symbol);
        nDesc = new BarBuilderNodeDescriptor();
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MINUTE, 5), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, nDesc, new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
		
//        BarBuilder<DataBar> builder1 = new BarBuilder<DataBar>(sub1);
        BarBuilderOld<DataBar> builder2 = new BarBuilderOld<DataBar>(sub2);
        
//        builder1.addChildNode(builder2);
//        builder1.setInputTimeSeries(sub1, new DataSeries<DataBar>(new Period(PeriodType.MINUTE, 1)));
//        DataSeries<DataBar> series1 = builder1.getInputTimeSeries(sub1);
//        assertNotNull(series1);
        
        builder2.addInputSubscription("Key", sub1);
        builder2.setInputTimeSeries(sub1, new DataSeries<DataBar>(new Period(PeriodType.MINUTE, 1)));
        DataSeries<DataBar> series2 = builder2.getInputTimeSeries(sub1);
        assertNotNull(series2);
        
        DataSeries<DataBar> series3 = builder2.getOutputTimeSeries(sub2);
        assertEquals(new Period(PeriodType.MINUTE, 5), series3.getPeriod());
        
        List<DataBar> list = getBars();
        SpanImpl span = new SpanImpl(new Period(PeriodType.MINUTE, 5),
        	ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 12, 0, 0).getMillis()),
        	ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 12, 30, 0).getMillis()));
        
        builder2.updateModifiedSpan(span, sub1);
        
        for(int i = 0;i < list.size();i++) {
        	series2.add(list.get(i));
        }
        
        builder2.process();
        
	}
	
	private List<DataBar> getBars() {
		int min = 0;
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 30;i++, min++) {
			DataBar db = new DataBar(new DateTime(2013, 12, 10, 12, min, 0), Period.ONE_MINUTE, 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

}
