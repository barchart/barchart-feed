package com.barchart.feed.series.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import com.barchart.feed.api.series.service.NodeDescriptor;
import com.barchart.feed.api.series.service.Query;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.api.series.temporal.TradingWeek;
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
	
	@Test
	public void testGetOrCreateIONode() {
		BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
		
		String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.HOUR, 12), dt2, null);
        SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        
        Node<SeriesSubscription> node = provider.getOrCreateIONode(sub, sub);
        
        
        assertNotNull(node);
        //Check that the node returned has the proper configuration
        assertEquals(sub, node.getOutputSubscriptions().get(0));
        
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
	
	@Test
    public void testGetOrCreateIONode_find_derivable() {
	    BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
        
        String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf = new TimeFrame(new Period(PeriodType.HOUR, 12), dt, null);
        SeriesSubscription sub = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf }, TradingWeek.DEFAULT);
        
        Node<SeriesSubscription> node = provider.getOrCreateIONode(sub, sub);
        
        Node<SeriesSubscription> derivable = node.getParentNodes().get(0).getParentNodes().get(0);
        Period p = ((AnalyticNode)derivable).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.MINUTE, p.getPeriodType());
        
        String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MINUTE, 12), dt2, null);
        SeriesSubscription sub2 = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
        
        Node<SeriesSubscription> found = provider.getOrCreateIONode(sub2, sub2);
        
        Node<SeriesSubscription> derivableMatch = found.getParentNodes().get(0);
        p = ((AnalyticNode)derivableMatch).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.MINUTE, p.getPeriodType());
        
        assertTrue(derivable == derivableMatch);
        assertTrue(provider.hasAssemblerParent(derivableMatch));
	}
	
	@Test
	public void testGetOrCreateNode() {
	    BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
	    NetworkSchema.setSchemaFilePath("testNetworks2.txt");
	    NetworkSchema.reloadDefinitions();
	    
	    String symbol = "ESZ13";
        Instrument instr = TestHarness.makeInstrument(symbol);
        DateTime dt1 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrame tf1 = new TimeFrame(new Period(PeriodType.MINUTE, 1), dt1, null);
        TimeFrame tf2 = new TimeFrame(new Period(PeriodType.MINUTE, 5), dt1, null);
        SeriesSubscription sub1 = new SeriesSubscription("ESZ13", instr, "PP_S4", new TimeFrame[] { tf1, tf2 }, TradingWeek.DEFAULT);
	    
        Query query = QueryBuilder.create().
			symbol("ESZ13").
			specifier("PP_S4").
			start(new DateTime(2013, 12, 10, 12, 0)).
			period(Period.ONE_MINUTE).
			period(new Period(PeriodType.MINUTE, 5)).build();
        
        SeriesSubscription fromQuery = (SeriesSubscription)query.toSubscription(instr);
        assertEquals(sub1, fromQuery);
        
        NodeDescriptor descriptor = provider.lookupDescriptor(query);
	    AnalyticNode node = provider.getOrCreateNode(sub1, sub1, (AnalyticNodeDescriptor)descriptor);
	    assertNotNull(node);
	    assertTrue(node.getChildNodes().isEmpty());
	    
	    assertEquals(fromQuery, node.getOutputSubscriptions().get(0));
	    assertEquals(1, node.getInputSubscriptions().size());
	    
	    Node<SeriesSubscription> parent = node.getParentNodes().get(0);
	    assertEquals(1, parent.getOutputSubscriptions().size());
	    assertEquals(2, parent.getInputSubscriptions().size());
	    SeriesSubscription compare1 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf1 }, TradingWeek.DEFAULT);
	    SeriesSubscription compare2 = new SeriesSubscription("ESZ13", instr, "IO", new TimeFrame[] { tf2 }, TradingWeek.DEFAULT);
	    //No way to pull them out in order, so test that they all exist using loop
	    SeriesSubscription[] control = new SeriesSubscription[] { compare1, compare2 };
	    for(SeriesSubscription s : control) {
	    	boolean comparesToSame = false;
	    	for(SeriesSubscription parentSub : parent.getInputSubscriptions()) {
	    		if(parentSub.equals(s)) {
	    			comparesToSame = true; break;
	    		}
	    	}
	    	if(!comparesToSame) fail();
	    }
	    
	    //Actual framework **DOES** differentiate timeframe ordering (and **MUST** to comply with Query).
	    AnalyticNode node0 = (AnalyticNode)parent.getParentNodes().get(0);
	    AnalyticNode node1 = (AnalyticNode)parent.getParentNodes().get(1);
	    assertNotNull(node0);
	    assertNotNull(node1);
	    AnalyticNode lowerTimeFrameParent = node0.getOutputTimeSeries("Overlay") == null ? node1 : node0;
	    AnalyticNode higherTimeFrameParent = lowerTimeFrameParent.equals(node1) ? node0 : node1;
	    
	    assertEquals(1, lowerTimeFrameParent.getOutputSubscriptions().get(0).getTimeFrames().length);
	    assertEquals(1, higherTimeFrameParent.getOutputSubscriptions().get(0).getTimeFrames().length);
	    
	    assertEquals(1, lowerTimeFrameParent.getOutputTimeSeries("Overlay", null).getPeriod().size());
	    assertEquals(5, higherTimeFrameParent.getOutputTimeSeries("Base", null).getPeriod().size());
	    
	    //First check path to root along "Overlay" 
	    parent = lowerTimeFrameParent;
	    Period p = ((AnalyticNode)node).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
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
        
        
        //Next check path to root along "Base"
        parent = higherTimeFrameParent;
	    p = ((AnalyticNode)node).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        p = ((AnalyticNode)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(5, p.size());
        assertEquals(PeriodType.MINUTE, p.getPeriodType());
        
        //////////// Must add this node to account for TimeFrame stepping down /////////////
        parent = parent.getParentNodes().get(0);
        p = ((AnalyticNode)parent).getOutputSubscriptions().get(0).getTimeFrame(0).getPeriod();
        assertEquals(1, p.size());
        assertEquals(PeriodType.MINUTE, p.getPeriodType());
        ////////////////////////////////////////////////////////////////////////////////////
        
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
	
	public static void main(String[] args) {
		new BarchartSeriesProviderTest().testFetch();
	}

}
