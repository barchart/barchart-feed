package com.barchart.feed.series.analytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.series.BarImpl;
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;
import com.barchart.feed.series.network.SeriesSubscription;
import com.barchart.feed.series.network.TestHarness;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.ValueFactory;

public class BarBuilderTest {
	private static final ValueFactory FACTORY = ValueFactoryImpl.getInstance();

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Test
	public void testMinuteTo5Minute() {
		String symbol = "ESZ13";
		Instrument instr = TestHarness.makeInstrument(symbol);
		DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
		TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 5), dt2, null);

		SeriesSubscription sub2 = new SeriesSubscription(
			"ESZ13", instr, "IO", new TimeFrameImpl[] {	tf2 }, TradingWeekImpl.DEFAULT);

		BarBuilder barBuilder = new BarBuilder(sub2);
		barBuilder.addInputTimeSeries(BarBuilder.INPUT_KEY, new DataSeriesImpl<BarImpl>(new Period(PeriodType.MINUTE, 1)));
		DataSeriesImpl<BarImpl> inputSeries =
			(DataSeriesImpl) barBuilder.getInputTimeSeries(BarBuilder.INPUT_KEY);
		assertNotNull(inputSeries);

		barBuilder.addOutputTimeSeries(BarBuilder.OUTPUT_KEY, new DataSeriesImpl<BarImpl>(new Period(PeriodType.MINUTE, 5)));
		DataSeriesImpl<BarImpl> outputSeries =
			(DataSeriesImpl) barBuilder.getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		assertNotNull(outputSeries);
		assertEquals(new Period(PeriodType.MINUTE, 5), outputSeries.getPeriod());

		List<BarImpl> list = getBars();
		SpanImpl span = new SpanImpl(new Period(PeriodType.MINUTE, 5),
			new DateTime(2013, 12, 10, 12, 0, 0),
				new DateTime(2013, 12, 10, 12, 30, 0));

		for (int i = 0; i < list.size(); i++) {
			inputSeries.add(list.get(i));
		}

		span.setNextDate(inputSeries.get(inputSeries.size() - 1).getDate());
		barBuilder.process(span);

		assertEquals(6, outputSeries.size());
		int mins = 5;
		for (int i = 0; i < outputSeries.size(); i++) {
			assertEquals(mins, outputSeries.get(i).getDate().getMinuteOfHour());
			mins += 5;
		}

		list = getBars2();
		for (BarImpl db : list) {
			inputSeries.add(db);
			span.setDate(db.getDate());
			span.setNextDate(db.getDate());
			barBuilder.process(span);
		}

		assertEquals(12, outputSeries.size());
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Test
	public void testSecondsToMinutes() {
		String symbol = "ESZ13";
		Instrument instr = TestHarness.makeInstrument(symbol);
		DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
		TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.MINUTE, 1), dt2, null);

		SeriesSubscription sub2 = new SeriesSubscription(
			"ESZ13", instr, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);

		BarBuilder barBuilder = new BarBuilder(sub2);
		barBuilder.addInputTimeSeries(BarBuilder.INPUT_KEY, new DataSeriesImpl<BarImpl>(new Period(PeriodType.SECOND, 1)));
		DataSeriesImpl<BarImpl> inputSeries =
			(DataSeriesImpl) barBuilder.getInputTimeSeries(BarBuilder.INPUT_KEY);
		assertNotNull(inputSeries);

		barBuilder.addOutputTimeSeries(BarBuilder.OUTPUT_KEY, new DataSeriesImpl<BarImpl>(new Period(PeriodType.MINUTE, 1)));
		DataSeriesImpl<BarImpl> outputSeries =
			(DataSeriesImpl) barBuilder.getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		assertNotNull(outputSeries);
		assertEquals(new Period(PeriodType.MINUTE, 1), outputSeries.getPeriod());

		List<BarImpl> list = getBars3();
		SpanImpl span = new SpanImpl(new Period(PeriodType.SECOND, 1),
			new DateTime(2013, 12, 10, 12, 0, 0),
				new DateTime(2013, 12, 10, 12, 0, 0));

		for (int i = 0; i < list.size(); i++) {
			inputSeries.add(list.get(i));
		}

		span.setNextDate(inputSeries.get(inputSeries.size() - 1).getDate());

		barBuilder.process(span);

		assertEquals(6, outputSeries.size());

		list = getBars4();
		for (BarImpl db : list) {
			inputSeries.add(db);
			span.setDate(db.getDate());
			span.setNextDate(db.getDate());
			barBuilder.process(span);
		}

		assertEquals(12, outputSeries.size());
		System.out.println("output series size: " + outputSeries.size());
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Test
	public void testSecondsToHours() {
		String symbol = "ESZ13";
		Instrument instr = TestHarness.makeInstrument(symbol);
		DateTime dt2 = new DateTime(2013, 12, 10, 0, 0, 0);
		TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 3), dt2, null);

		SeriesSubscription sub2 = new SeriesSubscription(
			"ESZ13", instr, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);

		BarBuilder barBuilder = new BarBuilder(sub2);
		barBuilder.addInputTimeSeries(BarBuilder.INPUT_KEY, new DataSeriesImpl<BarImpl>(
				new Period(PeriodType.SECOND, 1)));
		DataSeriesImpl<BarImpl> inputSeries =
				(DataSeriesImpl) barBuilder.getInputTimeSeries(BarBuilder.INPUT_KEY);
		assertNotNull(inputSeries);

		barBuilder.addOutputTimeSeries(BarBuilder.OUTPUT_KEY, new DataSeriesImpl<BarImpl>(
			new Period(PeriodType.HOUR, 3)));
		DataSeriesImpl<BarImpl> outputSeries =
			(DataSeriesImpl) barBuilder.getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		assertNotNull(outputSeries);
		assertEquals(new Period(PeriodType.HOUR, 3), outputSeries.getPeriod());

		List<BarImpl> list = getBars5();
		System.out.println("got " + list.size() + " bars");
		SpanImpl span = new SpanImpl(new Period(PeriodType.SECOND, 1),
			new DateTime(2013, 12, 10, 0, 0, 0),
				new DateTime(2013, 12, 10, 0, 0, 0));

		for (int i = 0; i < list.size(); i++) {
			inputSeries.add(list.get(i));
		}

		System.out.println("added " + list.size() + " bars to inputSeries");

		span.setNextDate(inputSeries.get(inputSeries.size() - 1).getDate());
		System.out.println("setNextDate to: " + inputSeries.get(inputSeries.size() - 1).getDate() + " --> " + span);
		System.out.println("calling process...");
		Span resultSpan = barBuilder.process(span);
		System.out.println("resultSpan = " + resultSpan);
		System.out.println("outputSeries size = " + outputSeries.size());
		System.out.println("process finished...");

		assertEquals(2, outputSeries.size());

		list = getBars6();
		for (BarImpl db : list) {
			inputSeries.add(db);
			span.setDate(db.getDate());
			span.setNextDate(db.getDate());
			barBuilder.process(span);
		}

		assertEquals(5, outputSeries.size());
		for (int i = 0; i < outputSeries.size(); i++) {
			System.out.println(outputSeries.get(i));
		}
		System.out.println("output series size: " + outputSeries.size());
	}

	private List<BarImpl> getBars() {
		int min = 0;
		List<BarImpl> l = new ArrayList<BarImpl>();
		for (int i = 0; i < 30; i++, min++) {
			BarImpl db = new BarImpl(null, new DateTime(2013, 12, 10, 12, min, 0), Period.ONE_MINUTE,
					FACTORY.newPrice(5),
					FACTORY.newPrice(5 + min),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5),
					FACTORY.newSize(5, 0),
					FACTORY.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

	private List<BarImpl> getBars2() {
		int min = 29;
		DateTime time = new DateTime(2013, 12, 10, 12, min, 0);
		List<BarImpl> l = new ArrayList<BarImpl>();
		for (int i = 0; i < 30; i++, min++) {
			BarImpl db = new BarImpl(null, time = time.plusMinutes(1), Period.ONE_MINUTE,
					FACTORY.newPrice(5),
					FACTORY.newPrice(5 + min + i),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5),
					FACTORY.newSize(5, 0),
					FACTORY.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

	private List<BarImpl> getBars3() {
		DateTime time = new DateTime(2013, 12, 10, 12, 0, 0);
		List<BarImpl> l = new ArrayList<BarImpl>();
		for (int i = 0; i < 360; i++) {
			BarImpl db = new BarImpl(null, time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5 + i),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5),
					FACTORY.newSize(5, 0),
					FACTORY.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

	private List<BarImpl> getBars4() {
		DateTime time = new DateTime(2013, 12, 10, 12, 6, 0);
		List<BarImpl> l = new ArrayList<BarImpl>();
		for (int i = 0; i < 360; i++) {
			BarImpl db = new BarImpl(null, time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5 + i),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5),
					FACTORY.newSize(5, 0),
					FACTORY.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

	// ///////////////

	private List<BarImpl> getBars5() {
		DateTime time = new DateTime(2013, 12, 10, 0, 0, 0);
		List<BarImpl> l = new ArrayList<BarImpl>();
		for (int i = 0; i < 32400; i++) {
			BarImpl db = new BarImpl(null, time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5 + i),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5),
					FACTORY.newSize(5, 0),
					FACTORY.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

	private List<BarImpl> getBars6() {
		DateTime time = new DateTime(2013, 12, 10, 9, 0, 0);
		List<BarImpl> l = new ArrayList<BarImpl>();
		for (int i = 0; i < 32400; i++) {
			BarImpl db = new BarImpl(null, time = time.plusSeconds(1), new Period(PeriodType.SECOND, 1),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5 + i),
					FACTORY.newPrice(5),
					FACTORY.newPrice(5),
					FACTORY.newSize(5, 0),
					FACTORY.newSize(5, 0));
			l.add(db);
		}
		return l;
	}

}
