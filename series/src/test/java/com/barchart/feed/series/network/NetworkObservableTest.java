package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;

import rx.Observer;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.NetworkNotification;
import com.barchart.feed.api.series.network.NetworkObservable;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;
import com.barchart.feed.series.network.BarchartSeriesProvider.SeriesSubscriber;

public class NetworkObservableTest {

	@Test
	public void test() {
		BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
		
		String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 12), dt2, null);
        SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);
        
        List<Node<SeriesSubscription>> nodes = new ArrayList<Node<SeriesSubscription>>();
        nodes.add(getTestNode());
		SeriesSubscriber ss = provider.new SeriesSubscriber(sub, nodes);
		Map<String, DataSeries<? extends DataPoint>> map = new HashMap<String, DataSeries<? extends DataPoint>>();
		NetworkObservable tso = new NetworkObservableImpl(ss, map);
		
		tso.subscribe(new Observer<NetworkNotification>() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(NetworkNotification args) {
				// TODO Auto-generated method stub
				
			}
			
		}, "IO");
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
