package com.barchart.feed.series.services;

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
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataSeries;


/**
 * 1. per symbol current feed and historical feed reception
 * 2. creation of output time series
 * 3. bar creation logic
 * 
 * @author David Ray
 *
 */
public class Distributor extends Node implements Assembler {
	private DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private SeriesSubscription subscription;
	
	private TimeSeries<?> outputTimeSeries;
	
	public Distributor() {
		
	}
	
	public Distributor(SeriesSubscription subscription) {
		this.subscription = subscription;
	}
	
	@Override
	public void onNextMarket(Market m) {
		System.out.println("1 onNextMarket: " + m.instrument().symbol() + ", " + m.trade().instrument().symbol());
		String symbol = m.trade().instrument().symbol();
		System.out.println(new StringBuilder(symbol).append(",").append(format.print(new DateTime(m.trade().time().millisecond()))).
			append("          ").append(format.print(new DateTime(m.trade().time().millisecond()))).append(m.trade().price().asDouble()).append(",").append(m.trade().size()));
	}
	
	@Override
	public <T extends HistoricalResult> void onNextHistorical(T result) {
		System.out.println("onNextHistorical: ");
		for(String s : result.getResult()) {
			System.out.println(s);
		}
	}

	@Override
	protected void updateModifiedSpan(Span span, Subscription subscription) {
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
	public List<Subscription> getOutputSubscriptions() {
		// TODO Auto-generated method stub
		return null;
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
