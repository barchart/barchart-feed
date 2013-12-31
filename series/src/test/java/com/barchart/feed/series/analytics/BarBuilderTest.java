package com.barchart.feed.series.analytics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.service.SeriesSubscription;
import com.barchart.feed.series.service.TestHarness;
import com.barchart.util.value.ValueFactoryImpl;

public class BarBuilderTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test() {
		String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.MINUTE, 1), dt1, null);
        
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        symbol = "ESZ13";
        instr = TestHarness.makeInstrument(symbol);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MINUTE, 5), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        BarBuilder barBuilder = new BarBuilder(sub2);
		barBuilder.addInputTimeSeries(BarBuilder.INPUT_KEY, new DataSeries<DataBar>(new Period(PeriodType.MINUTE, 1)));
		DataSeries<DataBar> inputSeries = (DataSeries)barBuilder.getInputTimeSeries(BarBuilder.INPUT_KEY);
		assertNotNull(inputSeries);
		
		barBuilder.addOutputTimeSeries(BarBuilder.OUTPUT_KEY, new DataSeries<DataBar>(new Period(PeriodType.MINUTE, 5)));
		DataSeries<DataBar> outputSeries = (DataSeries)barBuilder.getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		assertNotNull(outputSeries);
		assertEquals(new Period(PeriodType.MINUTE, 5), outputSeries.getPeriod());
		
		List<DataBar> list = getBars();
        SpanImpl span = new SpanImpl(new Period(PeriodType.MINUTE, 5),
        	ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 12, 0, 0).getMillis()),
        		ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 12, 30, 0).getMillis()));
        
        barBuilder.process(span);
        
        for(int i = 0;i < list.size();i++) {
        	inputSeries.add(list.get(i));
        }
        
        System.out.println("size = " + outputSeries.size());
        
	}
	
	private List<DataBar> getBars() {
		int min = 0;
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 30;i++, min++) {
			DataBar db = new DataBar(new DateTime(2013, 12, 10, 12, min, 0), Period.ONE_MINUTE, 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5 + min),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

}
