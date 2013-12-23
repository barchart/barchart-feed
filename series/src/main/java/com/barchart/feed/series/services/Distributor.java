package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Assembler;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;


/**
 * 
 * @author David Ray
 *
 */
public class Distributor extends Node implements Assembler {
	private DateTimeFormatter tickFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private DateTimeFormatter minuteFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	
	private SeriesSubscription subscription;
	private Period period;
	
	private TimeSeries<?> outputTimeSeries;
	
	private List<Subscription> outputSubscriptions;
	
	private DateTime last = null;
	
	private Object lock = new Object();
	
	public Distributor() {
		
	}
	
	public Distributor(SeriesSubscription subscription) {
		this.subscription = subscription;
		this.period = subscription.getTimeFrames()[0].getPeriod();
		this.outputSubscriptions = new ArrayList<Subscription>();
		this.outputSubscriptions.add(subscription);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onNextMarket(Market m) {
		System.out.println("onNextMarket: " + m.instrument().symbol() + ", " + m.trade().instrument().symbol() + ", " + m.trade().price().asDouble());
		DataBar bar = null;
		synchronized(lock) {
		    DataSeries<DataBar> series = (DataSeries)getOutputTimeSeries(subscription);
		    bar = new DataBar(m.trade().time(), period, null, null, null, m.trade().price(), m.trade().size(), null);
		    series.insertData(bar);
		}
		updateModifiedSpan(new SpanImpl(period, bar.getTime(), bar.getTime()), subscription);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends HistoricalResult> void onNextHistorical(T result) {
		System.out.println("onNextHistorical: ");
		
		SpanImpl span = null;
		DataSeries<DataBar> series = (DataSeries)getOutputTimeSeries(subscription);
		List<String> results = result.getResult();
		
		synchronized(lock) {
		    DataBar bar = null;
			boolean isFirst = false;
			for(String s : results) {	
				String[] resultArray = s.split("[\\,]+");
				
				bar = resultArray.length > 5 ? 
				    createBarFromMinuteCSV(resultArray) : 
				        createBarFromTickCSV(resultArray);
				
				if(!isFirst) {
				    isFirst = true;
					span = new SpanImpl(period, bar.getTime(), bar.getTime());
				}else{
					span.setNextTime(bar.getTime());
				}
				series.insertData(bar);
			}
		}
		
		updateModifiedSpan(span, subscription);
	}
	
	private DataBar createBarFromTickCSV(String[] array) {
		DataBar retVal = null;
		
		DateTime date = tickFormat.parseDateTime(array[0]);
		if(last != null && last.getMillis() >= date.getMillis()) {
			while(last.getMillis() >= date.getMillis())
				date = date.plusMillis(1);
		}
		Time time = ValueFactoryImpl.factory.newTime(date.getMillis());
		last = new DateTime(date.getMillis());
		
		Price value = ValueFactoryImpl.factory.newPrice(Double.parseDouble(array[3]));
		Size volume = ValueFactoryImpl.factory.newSize(Integer.parseInt(array[4]), 0);
		
		retVal = new DataBar(time, this.period, null, null, null, value, volume, null);
		
		return retVal;
	}
	
	private DataBar createBarFromMinuteCSV(String[] array) {
		DataBar retVal = null;
		
		DateTime date = minuteFormat.parseDateTime(array[0]);
		if(last != null && last.getMillis() >= date.getMillis()) {
			while(last.getMillis() >= date.getMillis())
				date = date.plusMillis(1);
		}
		last = new DateTime(date.getMillis());
		
		Time time = ValueFactoryImpl.factory.newTime(date.getMillis());
		Price open = ValueFactoryImpl.factory.newPrice(Double.parseDouble(array[2]));
		Price high = ValueFactoryImpl.factory.newPrice(Double.parseDouble(array[3]));
		Price low = ValueFactoryImpl.factory.newPrice(Double.parseDouble(array[4]));	
		Price close = ValueFactoryImpl.factory.newPrice(Double.parseDouble(array[5]));
		Size volume = ValueFactoryImpl.factory.newSize(Integer.parseInt(array[6]), 0);
		
		retVal = new DataBar(time, this.period, open, high, low, close, volume, null);
		
		return retVal;
	}

	@Override
	protected void updateModifiedSpan(Span span, Subscription subscription) {
		for(Node n : childNodes) {
			n.setModifiedSpan(new SpanImpl((SpanImpl)span), getOutputSubscriptions());
		}
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
	public List<Subscription> getOutputSubscriptions() {
		return outputSubscriptions;
	}

	@Override
	public List<Subscription> getInputSubscriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the output {@link TimeSeries}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription) {
		if(outputTimeSeries == null) {
			this.outputTimeSeries = new DataSeries<DataBar>(subscription.getTimeFrames()[0].getPeriod());
		}
		return (TimeSeries<E>)this.outputTimeSeries;
	}

	@Override
	protected <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node[] lookup(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String getDerivableOutputKey(Subscription subscription) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Subscription getDerivableOutputSubscription(Subscription subscription) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public Subscription getSubscription() {
		return this.subscription;
	}

}
