package com.barchart.feed.series.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Subscriber;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.api.series.service.HistoricalObserver;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.api.series.service.HistoricalService;
import com.barchart.feed.series.network.QueryBuilderImpl;
import com.barchart.feed.series.network.SeriesSubscription;
import com.barchart.feed.series.network.TestHarness;

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
	/** Test Query for minutes - can be used as convenient means of constructing {@link Query} or {@link Subscription}*/
	public static final Query DEFAULT_MINUTE_QUERY = QueryBuilderImpl.create().
		symbol("ESZ13").
		instrument(TestHarness.makeInstrument("ESZ13").id()).
		start(new DateTime(2013, 12, 10, 9, 0)).
		end(new DateTime(2013, 12, 10, 12, 0)).
		period(Period.ONE_MINUTE).
		build();
	/** Test Query for ticks - can be used as convenient means of constructing {@link Query} or {@link Subscription} */
	public static final Query DEFAULT_TICK_QUERY = QueryBuilderImpl.create().
		symbol("ESZ13").
		instrument(TestHarness.makeInstrument("ESZ13").id()).
		start(new DateTime(2013, 12, 10, 9, 0)).
		end(new DateTime(2013, 12, 10, 12, 0)).
		period(new Period(PeriodType.TICK, 1)).
		build();

	private static final List<String> TICK_DATA = loadFromFile("/fauxTickData.txt");
	private static final List<String> MINUTE_DATA = loadFromFile("/fauxMinuteData.txt");


	/**
	 * Constructs a new {@code FauxHistoricalService} for testing.
	 * @param func	can pass in null or an Observable of your choice
	 */
	public FauxHistoricalService(final rx.Observable.OnSubscribe<HistoricalResult> func) {
		super(func != null ? func : new Observable.OnSubscribe<HistoricalResult>() {
			@Override
			public void call(final Subscriber<? super HistoricalResult> t1) {
				t1.add(new SeriesSubscription());
			};
		});
	}

	@Override
	public <S extends Subscription> void subscribe(final HistoricalObserver<HistoricalResult> observer, final S subscription) {
		if(subscription.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.TICK) {
			final HistoricalResult result = new HistoricalResult() {
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
			final HistoricalResult result = new HistoricalResult() {
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
	public <S extends Subscription> void subscribe(final HistoricalObserver<HistoricalResult> observer, final S subscription, final Query customQuery) {
		throw new UnsupportedOperationException("This version of subscribe() not supported in this test class.");
	}

	private static List<String> loadFromFile(final String filename) {
		final List<String> retVal = new ArrayList<String>();

		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(
				FauxHistoricalService.class.getResourceAsStream(filename)));
			String line = null;
			while((line = buff.readLine()) != null) {
				if((line = line.trim()).length() < 1) continue;
				retVal.add(line);
			}
		}catch(final Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}


}
