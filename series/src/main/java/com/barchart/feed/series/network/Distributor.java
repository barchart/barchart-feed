package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.analytics.Assembler;
import com.barchart.feed.api.series.analytics.Node;
import com.barchart.feed.api.series.analytics.Subscription;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.series.BarImpl;
import com.barchart.feed.series.DataPointImpl;
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.SpanImpl;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;


/**
 * Implementation of {@link Assembler} which receives event based raw data 
 * from two different sources (historical batched, and market tick data), and
 * output that data to a single {@link DataSeries} that is then consumed by child
 * nodes which are linked up to this node's output. 
 * 
 * @author David Ray
 *
 */
public class Distributor extends Node<SeriesSubscription> implements Assembler {
    /** Size parameter used to distinguish a line of tick data from minute data*/
    private static final int TICK_FORMAT_LENGTH = 5;
    /** Formatter to operate on dates as they appear in batched tick query results */
	private DateTimeFormatter tickFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	/** Formatter to operate on dates as they appear in batched minute query results */
	private DateTimeFormatter minuteFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	/** {@link Subscription} describing this distributor's output */
	private SeriesSubscription subscription;
	/** Holds the {@link Period} of this {@code Distributor}'s output {@link Subscription} */
	private Period period;
	/** The {@link DataSeries} object containing this node's output */
	private DataSeriesImpl<?> outputTimeSeries;
	/** List containing this {@code Distributor}'s one output {@link Subscription} */
	private List<SeriesSubscription> outputSubscriptions;
	/** The last date processed */
	private DateTime last = null;
	/** Monitor to synchronize historical and live updates during original load */
	private Object lock = new Object();
	/** Queue to synchronize updated time {@link Span}s. */
	private ConcurrentLinkedQueue<Span> dataQueue;
	
	
	/**
	 * Constructs a new {@code Distributor}
	 */
	public Distributor() {}
	
	/**
	 * Constructs a new functional {@code Distributor} 
	 * @param subscription     the {@link SeriesSubscription} supplying needed init params.
	 */
	public Distributor(SeriesSubscription subscription) {
		this.subscription = subscription;
		this.period = subscription.getTimeFrames()[0].getPeriod();
		this.outputSubscriptions = new ArrayList<SeriesSubscription>();
		this.outputSubscriptions.add(subscription);
		this.dataQueue = new ConcurrentLinkedQueue<Span>();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onNextMarket(Market m) {
		System.out.println("onNextMarket: " + m.instrument().symbol() + ", " + m.trade().instrument().symbol() + ", " + m.trade().price().asDouble());
		BarImpl bar = null;
		synchronized(lock) {
		    DataSeriesImpl<BarImpl> series = (DataSeriesImpl)getOutputTimeSeries(subscription);
		    bar = new BarImpl(m.trade().time(), period, null, null, null, m.trade().price(), m.trade().size(), null);
		    series.insertData(bar);
		    
		    setModifiedSpan(new SpanImpl(period, bar.getTime(), bar.getTime()), outputSubscriptions);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends HistoricalResult> void onNextHistorical(T result) {
		System.out.println("onNextHistorical: ");
		
		SpanImpl span = null;
		DataSeriesImpl<BarImpl> series = (DataSeriesImpl)getOutputTimeSeries(subscription);
		List<String> results = result.getResult();
		
		synchronized(lock) {
		    BarImpl bar = null;
			boolean isFirst = false;
			for(String s : results) {	
				String[] resultArray = s.split("[\\,]+");
				
				bar = resultArray.length > TICK_FORMAT_LENGTH ? 
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
			
			setModifiedSpan(span, outputSubscriptions);
		}
	}
	
	private BarImpl createBarFromTickCSV(String[] array) {
		BarImpl retVal = null;
		
		DateTime date = tickFormat.parseDateTime(array[0]);
		if(last != null && last.getMillis() >= date.getMillis()) {
			while(last.getMillis() >= date.getMillis())
				date = date.plusMillis(1);
		}
		Time time = ValueFactoryImpl.factory.newTime(date.getMillis());
		last = new DateTime(date.getMillis());
		
		Price value = ValueFactoryImpl.factory.newPrice(Double.parseDouble(array[3]));
		Size volume = ValueFactoryImpl.factory.newSize(Integer.parseInt(array[4]), 0);
		
		retVal = new BarImpl(time, this.period, null, null, null, value, volume, null);
		
		return retVal;
	}
	
	private BarImpl createBarFromMinuteCSV(String[] array) {
		BarImpl retVal = null;
		
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
		
		retVal = new BarImpl(time, this.period, open, high, low, close, volume, null);
		
		return retVal;
	}

	@Override
	protected <S extends Span> void updateModifiedSpan(S span, SeriesSubscription subscription) {
		dataQueue.offer((S) span);
		setUpdated(true);
	}

	@Override
	protected boolean hasAllAncestorUpdates() {
		return true;
	}

	@Override
	protected Span process() {
		Span span = dataQueue.poll();
		System.out.println(this + " processing span: " + span);
		return span;
	}

	@Override
	public List<SeriesSubscription> getOutputSubscriptions() {
		return outputSubscriptions;
	}

	@Override
	public List<SeriesSubscription> getInputSubscriptions() {
		return outputSubscriptions;
	}

	/**
	 * Returns the output {@link DataSeries}
	 */
	@SuppressWarnings("unchecked")
	public <E extends DataPointImpl> DataSeriesImpl<E> getOutputTimeSeries(Subscription subscription) {
		if(outputTimeSeries == null) {
			this.outputTimeSeries = new DataSeriesImpl<BarImpl>(subscription.getTimeFrames()[0].getPeriod());
		}
		return (DataSeriesImpl<E>)this.outputTimeSeries;
	}

	@Override
	public boolean isDerivableSource(SeriesSubscription subscription) {
		return false;
	}

    @Override
    public SeriesSubscription getDerivableOutputSubscription(SeriesSubscription subscription) {
        throw new UnsupportedOperationException("Assemblers do not support derivation");
    }

    @Override
	public Subscription getSubscription() {
		return this.subscription;
	}
    
    public String toString() {
        StringBuilder sb = new StringBuilder("ASSEMBLER").append(": ").append(" ---> ").append(subscription);
        return sb.toString();
    }

}
