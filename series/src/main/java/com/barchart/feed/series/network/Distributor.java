package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.Assembler;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.series.BarImpl;
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.SpanImpl;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.ValueFactory;


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
    /** Factory for creating value-api objects */
    private static final ValueFactory valueFactory = ValueFactoryImpl.getInstance();

    /** Formatter to operate on dates as they appear in batched tick query results */
	private DateTimeFormatter tickFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	/** Formatter to operate on dates as they appear in batched minute query results */
	private DateTimeFormatter minuteFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

	/** {@link Subscription} describing this distributor's output */
	private SeriesSubscription subscription;
	/** Holds the {@link Period} of this {@code Distributor}'s output {@link Subscription} */
	private Period period;
	/** The {@link DataSeries} object containing this node's output */
	private DataSeries<?> outputTimeSeries;
	/** List containing this {@code Distributor}'s one output {@link Subscription} */
	private List<SeriesSubscription> outputSubscriptions;
	/** The last date processed */
	private DateTime last = null;
	/** Monitor to synchronize historical and live updates during original load */
	private boolean historicalDataAdded = false;
	/** Records the span of historical data received. */
	private SpanImpl historicalSpan = SpanImpl.INITIAL;
	
	/** Queue to synchronize updated time {@link Span}s. */
	private ConcurrentLinkedQueue<BarImpl> dataQueue;
	private ConcurrentLinkedQueue<BarImpl> historicalQueue;
	
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
		this.outputSubscriptions = new ArrayList<SeriesSubscription>(1);
		this.outputSubscriptions.add(subscription);
		this.dataQueue = new ConcurrentLinkedQueue<BarImpl>();
		this.historicalQueue = new ConcurrentLinkedQueue<BarImpl>();
	}

	/**
	 * Returns this Node's name
	 * @return     this Node's name.
	 */
	@Override
	public String getName() {
	    return subscription.toString();
	}

	@Override
	public void onNextMarket(Market m) {
	    //if(true) return;
		final BarImpl bar =
				new BarImpl(m.instrument().id(), m.trade().time(), period, m.trade().price(), m.trade().price(), m
						.trade().price(), m.trade().price(), m.trade().size(), null);
		dataQueue.offer(bar);
//		System.out.println("onNextMarket: " + m.instrument().symbol() + ", " + m.trade().price().asDouble() + ",  " + new SpanImpl(period, bar.getTime(), bar.getTime()));
        if(historicalDataAdded) {
            System.out.println("HISTORICAL DATA ADDED");
			setModifiedSpan(new SpanImpl(period, bar.getDate(), bar.getDate()), outputSubscriptions);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends HistoricalResult> void onNextHistorical(final T result) {
//		System.out.println("onNextHistorical: ");

		BarImpl bar = null;
        boolean executedOnce = false;
        SpanImpl span = null;
        final DataSeriesImpl<BarImpl> series = (DataSeriesImpl)getOutputTimeSeries(subscription);
        final List<String> results = result.getResult();

        for(final String s : results) {
            final String[] resultArray = s.split("[\\,]+");

            bar = resultArray.length > TICK_FORMAT_LENGTH ?
                createBarFromMinuteCSV(resultArray) :
                    createBarFromTickCSV(resultArray);

            if(!executedOnce) {
                executedOnce = true;
				span = new SpanImpl(period, bar.getDate(), bar.getDate());
            }else{
				span.setNextDate(bar.getDate());
            }

            historicalQueue.add(bar);
        }

        setModifiedSpan(span, outputSubscriptions);

        //historicalDataAdded = true;
	}

	private BarImpl createBarFromTickCSV(String[] array) {
		BarImpl retVal = null;

		DateTime date = tickFormat.parseDateTime(array[0]);
		if(last != null && last.getMillis() >= date.getMillis()) {
			while(last.getMillis() >= date.getMillis())
				date = date.plusMillis(1);
		}
		last = new DateTime(date.getMillis());

		final Price value = valueFactory.newPrice(Double.parseDouble(array[3]));
		final Size volume = valueFactory.newSize(Integer.parseInt(array[4]), 0);

		retVal = new BarImpl(null, last, this.period, value, value, value, value, volume, null);

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

		final Time time = valueFactory.newTime(date.getMillis());
		final Price open = valueFactory.newPrice(Double.parseDouble(array[2]));
		final Price high = valueFactory.newPrice(Double.parseDouble(array[3]));
		final Price low = valueFactory.newPrice(Double.parseDouble(array[4]));
		final Price close = valueFactory.newPrice(Double.parseDouble(array[5]));
		final Size volume = valueFactory.newSize(Integer.parseInt(array[6]), 0);

		retVal = new BarImpl(null, time, this.period, open, high, low, close, volume, null);

		return retVal;
	}

	@Override
	protected <S extends Span> void updateModifiedSpan(S span, final SeriesSubscription subscription) {
		setUpdated(true);
	}

	@Override
	protected boolean hasAllAncestorUpdates() {
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	protected Span process() {
	    SpanImpl span = null;
	    if(!historicalQueue.isEmpty()) {
	        BarImpl next = null;
            final DataSeriesImpl<BarImpl> series = (DataSeriesImpl)getOutputTimeSeries(subscription);
            while((next = historicalQueue.poll()) != null) {
                if(span == null) {
					span = new SpanImpl(subscription.getTimeFrame(0).getPeriod(), next.getDate(), next.getDate());
                }
				span.setNextDate(next.getDate());
                series.add(next);
            }
	    }else if(!dataQueue.isEmpty()) {
	        BarImpl next = null;
	        final DataSeriesImpl<BarImpl> series = (DataSeriesImpl)getOutputTimeSeries(subscription);
	        while((next = dataQueue.poll()) != null) {
	            if(span == null) {
					span = new SpanImpl(subscription.getTimeFrame(0).getPeriod(), next.getDate(), next.getDate());
	            }
				span.setNextDate(next.getDate());
	            series.add(next);
            }
	    }

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
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(Subscription subscription) {
		if(outputTimeSeries == null) {
			this.outputTimeSeries = new DataSeriesImpl<BarImpl>(subscription.getTimeFrames()[0].getPeriod());
		}
		return (DataSeries<E>)this.outputTimeSeries;
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

    @Override
	public String toString() {
        final StringBuilder sb = new StringBuilder("Assembler: ").append(" Distributor ").append(subscription);
        return sb.toString();
    }

}
