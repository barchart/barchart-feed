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
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.TradingWeekImpl;
import com.barchart.feed.series.analytics.BarBuilder;
import com.barchart.feed.series.network.BarchartSeriesProvider.SeriesSubscribeFunc;
import com.barchart.util.value.ValueFactoryImpl;

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
        nodes.add(getTestNode(sub.toString()));
		SeriesSubscribeFunc ss = provider.new SeriesSubscribeFunc(sub, nodes);
		Map<String, DataSeries<? extends DataPoint>> map = new HashMap<String, DataSeries<? extends DataPoint>>();
		
		Observer<NetworkNotification> obs = getTestObserver();
		NetworkObservable no = new NetworkObservableImpl(ss, map);
		no.subscribe(obs, sub.toString());
	}
	
	@Test
    public void testRegisterTo_before_subscribing() {
        BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
        
        String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 12), dt2, null);
        SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);
        
        List<Node<SeriesSubscription>> nodes = new ArrayList<Node<SeriesSubscription>>();
        nodes.add(getTestNode(sub.toString()));
        SeriesSubscribeFunc ss = provider.new SeriesSubscribeFunc(sub, nodes);
        Map<String, DataSeries<? extends DataPoint>> map = new HashMap<String, DataSeries<? extends DataPoint>>();
        
        Observer<NetworkNotification> obs = getTestObserver();
        NetworkObservable no = new NetworkObservableImpl(ss, map);
        no.registerTo(obs, sub.toString());
        no.subscribe(obs);
    }
	
	@Test
    public void testSubscribeAll() {
        BarchartSeriesProvider provider = new BarchartSeriesProvider(null);
        
        String symbol2 = "ESZ13";
        Instrument instr2 = TestHarness.makeInstrument(symbol2);
        DateTime dt2 = new DateTime(2013, 12, 10, 12, 0, 0);
        TimeFrameImpl tf2 = new TimeFrameImpl(new Period(PeriodType.HOUR, 12), dt2, null);
        SeriesSubscription sub = new SeriesSubscription("ESZ13", instr2, "IO", new TimeFrameImpl[] { tf2 }, TradingWeekImpl.DEFAULT);
        
        AnalyticNode node = (AnalyticNode)getTestNode(sub.toString());
        List<Node<SeriesSubscription>> nodes = new ArrayList<Node<SeriesSubscription>>();
        nodes.add(node);
        SeriesSubscribeFunc ss = provider.new SeriesSubscribeFunc(sub, nodes);
        Map<String, DataSeries<? extends DataPoint>> map = new HashMap<String, DataSeries<? extends DataPoint>>();
        node.addOutputKeyMapping(BarBuilder.OUTPUT_KEY, sub);
        DataSeries<DataPoint> series = node.getOutputTimeSeries(sub);
        map.put(sub.toString(), series);
        
        Observer<NetworkNotification> obs = getTestObserver();
        NetworkObservable no = new NetworkObservableImpl(ss, map);
        no.subscribeAll(obs);
        
        node.get
    }
	
	private Observer<NetworkNotification> getTestObserver() {
	    return new Observer<NetworkNotification>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(NetworkNotification args) {
            }
            
        };
	}
	
	private Node<SeriesSubscription> getTestNode(final String name) {
	    return new AnalyticNode(new TestAnalytic(name));
	}
	
	class TestAnalytic extends AnalyticBase {

	    public TestAnalytic(String name) {
	        setName(name);
	    }
        @Override
        public Span process(Span span) {
            return new SpanImpl(new Period(PeriodType.HOUR, 12), 
               ValueFactoryImpl.factory.newTime(new DateTime().minusHours(12).getMillis()), 
                   ValueFactoryImpl.factory.newTime(new DateTime().getMillis()));
        }
	    
	}

}
