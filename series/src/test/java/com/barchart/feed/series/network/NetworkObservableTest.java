package com.barchart.feed.series.network;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;
import com.barchart.feed.series.network.BarchartSeriesProvider.SeriesSubscribeFunc;
import com.barchart.util.value.ValueFactoryImpl;

public class NetworkObservableTest {
	private static final ValueFactoryImpl FACTORY = new ValueFactoryImpl();

	@Test
	public void testSubscribe() {
		final String symbol2 = "ESZ13";
		final Instrument instr2 = TestHarness.makeInstrument(symbol2);
		final DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
		final TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 12), dt2, null);
		final SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrameImpl[] {
				tf2
		}, TradingWeekImpl.DEFAULT);

		final BarchartSeriesProvider provider = TestHarness.getTestSeriesProvider(sub);

		final List<Node<SeriesSubscription>> nodes = new ArrayList<Node<SeriesSubscription>>();
		nodes.add(getTestNode(sub.toString()));
		final SeriesSubscribeFunc ss = provider.new SeriesSubscribeFunc(sub, nodes);
		final Map<String, DataSeries<? extends DataPoint>> map = new HashMap<String, DataSeries<? extends DataPoint>>();
		map.put(sub.toString(), new DataSeriesImpl<DataPoint>(new Period(PeriodType.HOUR, 12)));

		final Observer<NetworkNotification> obs = getTestObserver();
		final NetworkObservable no = new NetworkObservableImpl(ss, map);
		final rx.Subscription subscription = no.subscribe(obs);

		assertNotNull(subscription);
		assertTrue(nodes.get(0).isRunning());
		subscription.unsubscribe(); // Test that we unsubscribe from the node,
									// and that it results in its shutdown
		assertTrue(!nodes.get(0).isRunning());
	}

	@Test
	public void testSubscribeAll() {
		final String symbol2 = "ESZ13";
		final Instrument instr2 = TestHarness.makeInstrument(symbol2);
		final DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
		final TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 12), dt2, null);
		final SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrameImpl[] {
				tf2
		}, TradingWeekImpl.DEFAULT);

		final BarchartSeriesProvider provider = TestHarness.getTestSeriesProvider(sub);

		final List<Node<SeriesSubscription>> nodes = new ArrayList<Node<SeriesSubscription>>();
		nodes.add(getTestNode(sub.toString()));
		nodes.add(getTestNode("ARBITRARY NAME"));

		final SeriesSubscribeFunc ss = provider.new SeriesSubscribeFunc(sub, nodes);
		final Map<String, DataSeries<? extends DataPoint>> map = new HashMap<String, DataSeries<? extends DataPoint>>();
		map.put(sub.toString(), new DataSeriesImpl<DataPoint>(new Period(PeriodType.HOUR, 12)));
		map.put("ARBITRARY NAME", new DataSeriesImpl<DataPoint>(new Period(PeriodType.HOUR, 12)));

		final Observer<NetworkNotification> obs = getTestObserver();
		final NetworkObservable no = new NetworkObservableImpl(ss, map);
		final rx.Subscription subscription = no.subscribe(obs);

		assertNotNull(subscription);
		assertTrue(nodes.get(0).isRunning());
		assertTrue(nodes.get(1).isRunning());
		subscription.unsubscribe();// Test that we unsubscribe from multiple
									// nodes, and that it results in their
									// shutdown
		assertTrue(!nodes.get(0).isRunning());
		assertTrue(!nodes.get(1).isRunning());
	}

	private Observer<NetworkNotification> getTestObserver() {
		return new Observer<NetworkNotification>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(final Throwable e) {
				e.printStackTrace();
				fail();
			}

			@Override
			public void onNext(final NetworkNotification args) {
			}

		};
	}

	private Node<SeriesSubscription> getTestNode(final String name) {
		return new AnalyticNode(new TestAnalytic(name));
	}

	class TestAnalytic extends AnalyticBase {

		public TestAnalytic(final String name) {
			setName(name);
		}

		@Override
		public Span process(final Span span) {
			return new SpanImpl(new Period(PeriodType.HOUR, 12),
					new DateTime().minusHours(12),
					new DateTime());
		}
	}

}
