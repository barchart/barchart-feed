package com.barchart.feed.series;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;

import rx.Observer;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.TimeSeriesObservable;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.series.network.BarchartSeriesProvider;
import com.barchart.feed.series.network.SeriesSubscription;
import com.barchart.feed.series.network.TestHarness;
import com.barchart.feed.series.network.BarchartSeriesProvider.SeriesSubscriber;

public class TimeSeriesObserverTest {

	@Test
	public void test() {
		BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
		
		String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 12), dt2, null);
        SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);
        
		SeriesSubscriber ss = provider.new SeriesSubscriber(sub, getTestNode());
		TimeSeriesObservable tso = new TimeSeriesObservable(ss, new DataSeriesImpl<DataPointImpl>(Period.ONE_HOUR)) {

			@Override
			public Query getQuery() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <E extends DataPoint> DataSeries<E> getTimeSeries() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <E extends DataPoint> Map<String, DataSeries<? extends DataPoint>> getTimeSeriesMap() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		tso.subscribe(new Observer<Span>() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext(Span args) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private Node<SeriesSubscription> getTestNode() {
		return new Node<SeriesSubscription>() {

			@Override
			public boolean isDerivableSource(SeriesSubscription subscription) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected <T extends Span> void updateModifiedSpan(T span,
					SeriesSubscription subscription) {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected boolean hasAllAncestorUpdates() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected Span process() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<SeriesSubscription> getOutputSubscriptions() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<SeriesSubscription> getInputSubscriptions() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SeriesSubscription getDerivableOutputSubscription(
					SeriesSubscription subscription) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}

}
