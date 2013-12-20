package com.barchart.feed.series.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.TimeSeriesObservable;
import com.barchart.util.test.concurrent.TestObserver;

public class BarchartSeriesProviderTest {

	@Test
	public void testFetch() {
		
		// Test was failing in Jenkins
		
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
			Span span = testObserver.sync().results.get(0);
			assertNotNull(span); 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	public static void main(String[] args) {
		new BarchartSeriesProviderTest().testFetch();
	}

}
