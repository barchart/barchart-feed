package com.barchart.feed.series.services;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.services.NodeDescriptor;
import com.barchart.feed.api.series.services.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;

public class SubscriptionTest {

	@Test
	public void testEquals() {
		Subscription sub1 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.ONE_MINUTE, new DateTime(2013, 12, 10, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
		
		Subscription sub2 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.ONE_MINUTE, new DateTime(2013, 12, 10, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
		
		assertEquals(sub1, sub2);
		
		///////////////////
		//Period = DAY but start day are same, we vary the hours but should not fail. 
		sub1 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.DAY, new DateTime(2013, 12, 10, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
		
		sub2 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.DAY, new DateTime(2013, 12, 10, 12, 0, 0), null) },
			TradingWeek.DEFAULT);
		
		assertEquals(sub1, sub2);
		
		///////////////////
		//Period = DAY but start day are not the same , should fail
		sub1 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.DAY, new DateTime(2013, 12, 10, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
		
		sub2 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.DAY, new DateTime(2013, 12, 11, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
			
			assertNotEquals(sub1, sub2);
	}
	
	/**
	 * We need to test:
	 * 1. Symbols are same
	 * 2. TimeFrames: (assuming sub1 is the "source" and sub2 is the candidate "sink")
	 * 		a. StartDates for sub2 are equal or sub2 is after sub1
	 * 		b. For all TimeFrames:
	 * 			I. sub1.PeriodType is lower than sub2.PeriodType
	 * 			II. sub1.PeriodType is lower than PeriodType.WEEK (one of [TICK, MINUTE, HOUR, DAY] )
	 * 			III. sub1.Period.Interval == 1
	 *          --OR--
	 *          I. sub1.PeriodType is equal than sub2.PeriodType
	 *          II. sub1.Period.Interval == 1
	 *      c. Sessions (TradingWeek(s)) are the same.
	 */	
	@Test
	public void testIsDerivable() {
		Subscription sub1 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.ONE_MINUTE, new DateTime(2013, 12, 10, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
		
		Subscription sub2 = new Subscription(
			new NodeDescriptor("IO"),
			new FauxMarketService("","").makeInstrument("ESZ13"),
			"ESZ13",
			new TimeFrame[] { new TimeFrame(Period.ONE_MINUTE, new DateTime(2013, 12, 10, 9, 0, 0), null) },
			TradingWeek.DEFAULT);
			
		assertTrue(sub2.isDerivableFrom(sub1));
		
		
	}
}
