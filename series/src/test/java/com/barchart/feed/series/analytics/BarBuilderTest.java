package com.barchart.feed.series.analytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Ignore;
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
	@Ignore
	public void testMinuteTo5Minute() {
		String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
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
        
        for(int i = 0;i < list.size();i++) {
        	inputSeries.add(list.get(i));
        }
        
        span.setNextDate(inputSeries.get(inputSeries.size() - 1).getDate());
        barBuilder.process(span);
        
        assertEquals(6, outputSeries.size());
        int mins = 5;
        for(int i = 0;i < outputSeries.size();i++) {
        	assertEquals(mins, outputSeries.get(i).getDate().getMinuteOfHour());
        	mins += 5;
        }
        
        list = getBars2();
        for(DataBar db : list) {
        	inputSeries.add(db);
        	span.setDate(db.getDate());
        	span.setNextDate(db.getDate());
        	barBuilder.process(span);
        }
        
        assertEquals(12, outputSeries.size());
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Ignore
	public void testSecondsToMinutes() {
		String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MINUTE, 1), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        BarBuilder barBuilder = new BarBuilder(sub2);
		barBuilder.addInputTimeSeries(BarBuilder.INPUT_KEY, new DataSeries<DataBar>(new Period(PeriodType.SECOND, 1)));
		DataSeries<DataBar> inputSeries = (DataSeries)barBuilder.getInputTimeSeries(BarBuilder.INPUT_KEY);
		assertNotNull(inputSeries);
		
		barBuilder.addOutputTimeSeries(BarBuilder.OUTPUT_KEY, new DataSeries<DataBar>(new Period(PeriodType.MINUTE, 1)));
		DataSeries<DataBar> outputSeries = (DataSeries)barBuilder.getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		assertNotNull(outputSeries);
		assertEquals(new Period(PeriodType.MINUTE, 1), outputSeries.getPeriod());
		
		List<DataBar> list = getBars3();
        SpanImpl span = new SpanImpl(new Period(PeriodType.SECOND, 1),
        	ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 12, 0, 0).getMillis()),
        		ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 12, 0, 0).getMillis()));
        
        for(int i = 0;i < list.size();i++) {
        	inputSeries.add(list.get(i));
        }
        
        span.setNextDate(inputSeries.get(inputSeries.size() - 1).getDate());
        
        barBuilder.process(span);
        
        assertEquals(6, outputSeries.size());
        
        list = getBars4();
        for(DataBar db : list) {
        	inputSeries.add(db);
        	span.setDate(db.getDate());
        	span.setNextDate(db.getDate());
        	barBuilder.process(span);
        }
        
        assertEquals(12, outputSeries.size());
        System.out.println("output series size: " + outputSeries.size());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSecondsToHours() {
		String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt2 = new DateTime(2013, 12, 10, 0, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.HOUR, 3), dt2, null);
        
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        BarBuilder barBuilder = new BarBuilder(sub2);
		barBuilder.addInputTimeSeries(BarBuilder.INPUT_KEY, new DataSeries<DataBar>(new Period(PeriodType.SECOND, 1)));
		DataSeries<DataBar> inputSeries = (DataSeries)barBuilder.getInputTimeSeries(BarBuilder.INPUT_KEY);
		assertNotNull(inputSeries);
		
		barBuilder.addOutputTimeSeries(BarBuilder.OUTPUT_KEY, new DataSeries<DataBar>(new Period(PeriodType.HOUR, 3)));
		DataSeries<DataBar> outputSeries = (DataSeries)barBuilder.getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		assertNotNull(outputSeries);
		assertEquals(new Period(PeriodType.HOUR, 3), outputSeries.getPeriod());
		
		List<DataBar> list = getBars5();
		System.out.println("got " + list.size() + " bars");
        SpanImpl span = new SpanImpl(new Period(PeriodType.SECOND, 1),
        	ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 0, 0, 0).getMillis()),
        		ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 10, 0, 0, 0).getMillis()));
        
        for(int i = 0;i < list.size();i++) {
        	inputSeries.add(list.get(i));
        }
        
        System.out.println("added " + list.size() + " bars to inputSeries");
        
        span.setNextDate(inputSeries.get(inputSeries.size() - 1).getDate());
        
        System.out.println("calling process...");
        barBuilder.process(span);
        System.out.println("process finished...");
        
        assertEquals(2, outputSeries.size());
        
        list = getBars6();
        for(DataBar db : list) {
        	inputSeries.add(db);
        	span.setDate(db.getDate());
        	span.setNextDate(db.getDate());
        	barBuilder.process(span);
        }
        
        assertEquals(5, outputSeries.size());
        for(int i = 0;i < outputSeries.size();i++) {
        	System.out.println(outputSeries.get(i));
        }
        System.out.println("output series size: " + outputSeries.size());
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
	
	private List<DataBar> getBars2() {
		int min = 29;
		DateTime time = new DateTime(2013, 12, 10, 12, min, 0);
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 30;i++, min++) {
			DataBar db = new DataBar(time = time.plusMinutes(1), Period.ONE_MINUTE, 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5 + min + i),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}
	
	private List<DataBar> getBars3() {
		DateTime time = new DateTime(2013, 12, 10, 12, 0, 0);
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 360;i++) {
			DataBar db = new DataBar(time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1), 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5 + i),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}
	
	private List<DataBar> getBars4() {
		DateTime time = new DateTime(2013, 12, 10, 12, 6, 0);
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 360;i++) {
			DataBar db = new DataBar(time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1), 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5 + i),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}
	
	/////////////////
	
	private List<DataBar> getBars5() {
		DateTime time = new DateTime(2013, 12, 10, 0, 0, 0);
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 32400;i++) {
			DataBar db = new DataBar(time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1), 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5 + i),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}
	
	private List<DataBar> getBars6() {
		DateTime time = new DateTime(2013, 12, 10, 9, 0, 0);
		List<DataBar> l = new ArrayList<DataBar>();
		for(int i = 0;i < 32400;i++) {
			DataBar db = new DataBar(time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1), 
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5 + i),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newPrice(5),
				ValueFactoryImpl.factory.newSize(5, 0),
				ValueFactoryImpl.factory.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

}
