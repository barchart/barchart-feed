package com.barchart.feed.series.services;

import rx.Observable;

import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.client.provider.BarchartMarketProvider;

/**
 * <pre>
 * 		BarchartSeriesProvider provider = new BarchartSeriesProvider(MarketService);
 * 		Query query = QueryBuilder.create().symbol("ESZ3").build();
 * 		Observable<TimeSeries> series = provider.subscribe(query);
 * </pre>
 * @author David Ray
 *
 */
public class BarchartSeriesProviderTest {
	private BarchartSeriesProvider provider;
	
	public void testInstantiate() {
		BarchartMarketProvider marketService = new BarchartMarketProvider("dray", "dray");
		provider = new BarchartSeriesProvider(marketService);
	}
	
	public Observable<TimeSeries<TimePoint>> testSubscribe() {
		return provider.subscribe(QueryBuilder.create().symbol("ESZ13").build());
	}
	
	public static void main(String[] args) {
		BarchartSeriesProviderTest test = new BarchartSeriesProviderTest();
		test.testInstantiate();
		
		//Test that the market service daemon thread doesn't exit and our service stays up and running.
		try { 
			System.out.println("test wait before first subscription - 15secs.");
			for(int i = 1;i < 16;i++) {
				Thread.sleep(1000);
				System.out.print(i + " ");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("now testing subscribe");
		Observable<TimeSeries<TimePoint>> observable = test.testSubscribe();
		
	}
}
