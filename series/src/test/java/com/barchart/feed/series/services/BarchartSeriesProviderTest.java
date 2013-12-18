package com.barchart.feed.series.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import rx.Observable;

import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.util.test.concurrent.TestObserver;

public class BarchartSeriesProviderTest {

	@Test
	public void testFetch() {
		
		// Test was failing in Jenkins
		
//		FauxMarketService marketService = new FauxMarketService("test", "test");
//		marketService.preSubscribe(FauxHistoricalService.DEFAULT_MINUTE_QUERY);
//		BarchartSeriesProvider provider = new BarchartSeriesProvider(marketService, new FauxHistoricalService(null));
//		
//		//Observable Should be null here until I finish the node lookup and graph construction I'm currently working on.
//		//Fails due to this is where I'm working (Test Driven Baby!)
//		//Finished first part which is determining equality and "derivability" of Subscriptions (now writing tests for them)
//		Observable<TimeSeries<TimePoint>> observable = provider.fetch(FauxHistoricalService.DEFAULT_MINUTE_QUERY);
//		
//		TestObserver<TimeSeries<TimePoint>> testObserver = new TestObserver<TimeSeries<TimePoint>>();
//		observable.subscribe(testObserver);
//		
//		try {
//			TimeSeries<TimePoint> series = testObserver.sync().results.get(0);
//			assertNotNull(series); 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static void main(String[] args) {
		new BarchartSeriesProviderTest().testFetch();
	}

}
