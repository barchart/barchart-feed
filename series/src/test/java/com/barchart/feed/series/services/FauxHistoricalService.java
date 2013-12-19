package com.barchart.feed.series.services;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Observer;

import com.barchart.feed.api.series.services.HistoricalObserver;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.HistoricalService;
import com.barchart.feed.api.series.services.Query;
import com.barchart.feed.api.series.services.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.series.services.QueryBuilder;

/**
 * Returns data for one minute query: 2013-12-10 09:00  --> 2013-12-10 11:59
 * --OR--
 * one tick query with the same start and end times.
 * 
 * This class is meant to be compatible with testing frameworks only.
 * 
 * @author David Ray
 *
 */
public class FauxHistoricalService extends HistoricalService<HistoricalResult>{
	/** Test Query for minutes - can be used as convient means of constructing {@link Query} or {@link Subscription}*/
	public static final Query DEFAULT_MINUTE_QUERY = QueryBuilder.create().
		symbol("ESZ13").
		start(new DateTime(2013, 12, 10, 9, 0)).
		end(new DateTime(2013, 12, 10, 12, 0)).
		period(Period.ONE_MINUTE).
		build();
	/** Test Query for ticks - can be used as convient means of constructing {@link Query} or {@link Subscription} */
	public static final Query DEFAULT_TICK_QUERY = QueryBuilder.create().
		symbol("ESZ13"). 
		start(new DateTime(2013, 12, 10, 9, 0)).
		end(new DateTime(2013, 12, 10, 12, 0)).
		period(new Period(PeriodType.TICK, 1)).
		build();
	
	private static final List<String> TICK_DATA = loadFromFile("fauxTickData.txt");
	private static final List<String> MINUTE_DATA = loadFromFile("fauxMinuteData.txt");
	
	
	/**
	 * Constructs a new {@code FauxHistoricalService} for testing.
	 * @param func	can pass in null or an Observable of your choice
	 */
	public FauxHistoricalService(rx.Observable.OnSubscribeFunc<HistoricalResult> func) {
		super(func != null ? func : new Observable.OnSubscribeFunc<HistoricalResult>() {
			@Override public SeriesSubscription onSubscribe(Observer<? super HistoricalResult> t1) {
				return new SeriesSubscription() { @Override public void unsubscribe() {} };
			};
		});
	}
	
	@Override
	public <S extends Subscription> void subscribe(HistoricalObserver<HistoricalResult> observer, final S subscription) {
		if(subscription.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.TICK) {
			HistoricalResult result = new HistoricalResult() {
				@Override
				public S getSubscription() {
					return subscription;
				}

				@Override
				public List<String> getResult() {
					return new ArrayList<String>(TICK_DATA);
				}
				
			};
			
			observer.onNext(result);
			observer.onCompleted();
			
		}else if(subscription.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.MINUTE) {
			HistoricalResult result = new HistoricalResult() {
				@Override
				public S getSubscription() {
					return subscription;
				}

				@Override
				public List<String> getResult() {
					return new ArrayList<String>(MINUTE_DATA);
				}
				
			};
			
			observer.onNext(result);
			observer.onCompleted();
		}else{
			throw new IllegalArgumentException("EEEK! Don't know what to do with that subscription period type yet!");
		}
	}
	@Override
	public <S extends Subscription> void subscribe(HistoricalObserver<HistoricalResult> observer, S subscription, Query customQuery) {
		throw new UnsupportedOperationException("This version of subscribe() not supported in this test class.");
	}
	
	private static List<String> loadFromFile(String filename) {
		List<String> retVal = new ArrayList<String>();
		
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(
				FauxHistoricalService.class.getResourceAsStream(filename)));
			String line = null;
			while((line = buff.readLine()) != null) {
				if((line = line.trim()).length() < 1) continue;
				retVal.add(line);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

    	
}
