package com.barchart.feed.series.service;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.TimeSeriesObservable;
import com.barchart.feed.api.series.service.Assembler;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
import com.barchart.feed.series.analytics.BarBuilder;
import com.barchart.util.test.concurrent.TestObserver;

public class BarchartSeriesProviderTest {

	@Ignore
	public void testFetch() {
		
		FauxMarketService marketService = new FauxMarketService("test", "test");
		marketService.preSubscribe(FauxHistoricalService.DEFAULT_MINUTE_QUERY);
		BarchartFeedService feed = new BarchartFeedService(marketService, new FauxHistoricalService(null));
		BarchartSeriesProvider provider = new BarchartSeriesProvider(feed);
		
		//Observable Should be null here until I finish the node lookup and graph construction I'm currently working on.
		//Fails due to this is where I'm working (Test Driven Baby!)
		//Finished first part which is determining equality and "derivability" of Subscriptions (now writing tests for them)
		TimeSeriesObservable observable = provider.fetch(FauxHistoricalService.DEFAULT_MINUTE_QUERY);
		TimeSeries<TimePoint> series = observable.getTimeSeries();
		assertNotNull(series);
		
		TestObserver<Span> testObserver = new TestObserver<Span>();
		observable.subscribe(testObserver);
		try {
			Span span = testObserver.sync(10000).results.get(0);
			assertNotNull(span); 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLookupIONode() {
		BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
		
		List<Node<SeriesSubscription>> ios = null;
		try {
			Field ioNodeListField = BarchartSeriesProvider.class.getDeclaredField("ioNodes");
			ioNodeListField.setAccessible(true);
			ios = (List<Node<SeriesSubscription>>)ioNodeListField.get(provider);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.TICK, 1), dt1, null);
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
        
        BarBuilderNodeDescriptor desc = new BarBuilderNodeDescriptor();
        desc.setAnalyticClass(BarBuilder.class);
        desc.setConstructorArg(sub1);
        AnalyticNode derivable = new AnalyticNode(desc.instantiateBuilderAnalytic());
        derivable.addOutputKeyMapping(BarBuilder.OUTPUT_KEY, sub1);
        //ios.add(derivable);
        
        String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.HOUR, 12), dt2, null);
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        
        Node<SeriesSubscription> node = provider.lookupIONode(sub2, sub2);
        
        
        assertNotNull(node);
        //Check that the node returned has the proper configuration
        assertEquals(sub2, node.getOutputSubscriptions().get(0));
        
        //Now check the internals to make sure proper references are configured
        Period p = ((AnalyticNode)node).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(12, p.size());
        assertEquals(PeriodType.HOUR, p.getPeriodType());
        
        Node<SeriesSubscription> parent = node.getParentNodes().get(0);
        p = ((AnalyticNode)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.HOUR, p.getPeriodType());
        
        parent = parent.getParentNodes().get(0);
        p = ((AnalyticNode)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.MINUTE, p.getPeriodType());
        
        parent = parent.getParentNodes().get(0);
        p = ((AnalyticNode)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.SECOND, p.getPeriodType());
        
        parent = parent.getParentNodes().get(0);
        p = ((AnalyticNode)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.TICK, p.getPeriodType());
        
        parent = parent.getParentNodes().get(0);
        assertTrue(Assembler.class.isAssignableFrom(parent.getClass()));
        p = ((Distributor)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.TICK, p.getPeriodType());
        
        assertEquals(0, parent.getParentNodes().size());
	}
	
	
	public void testGetNode() {
	    BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
	}
	
	public static void main(String[] args) {
		new BarchartSeriesProviderTest().testFetch();
	}

}
