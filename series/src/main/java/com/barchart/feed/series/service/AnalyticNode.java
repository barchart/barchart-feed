package com.barchart.feed.series.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.service.AnalyticContainer;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.series.CalculationImpl;
import com.barchart.feed.series.DataPoint;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;
import com.barchart.util.value.ValueFactoryImpl;

public class AnalyticNode<E extends DataPoint> extends Node implements AnalyticContainer {
	public enum SpanOperation { UNION, INTERSECTION };
	
	/** Holds the keys which describe the input mappings and are used to correlate the inputs with their {@link SeriesSubscription}s */
	private Map<String, SeriesSubscription> inputKeyMap = new ConcurrentHashMap<String, SeriesSubscription>();
	/** Holds the keys which describe the output mappings and are used to correlate the output with their {@link SeriesSubscription}s */
	private Map<String, SeriesSubscription> outputKeyMap = new ConcurrentHashMap<String, SeriesSubscription>();
	
	/** Maps a given {@link TimeSeries} to a {@link Subscription} */
	private Map<SeriesSubscription, DataSeries<E>> inputTimeSeries = new ConcurrentHashMap<SeriesSubscription, DataSeries<E>>();
	/** Maps a given {@link TimeSeries} to a {@link Subscription} */
	private Map<SeriesSubscription, DataSeries<E>> outputTimeSeries = new ConcurrentHashMap<SeriesSubscription, DataSeries<E>>();
	
	/** Contains all currently input {@link SeriesSubscriptions}, to be tested on each process cycle to see if all required inputs have been received */
	private Map<SeriesSubscription, SpanImpl> currentUpdates = Collections.synchronizedMap(new HashMap<SeriesSubscription, SpanImpl>());
	
	/** The currently updated {@link Span} for the current process cycle */
	private volatile AtomicReference<SpanImpl> currentUpdateSpan = new AtomicReference<SpanImpl>();
	
	/** Owned Span object internally mapped in child nodes of the update Span used during processing */
	private SpanImpl currentProcessSpan;
	
	/** The specific analytic indicator type */
	private Analytic analytic;

	
	/**
	 * Constructs a new {@code AnalyticNode} to "hook in" the specified
	 * Processor to the analytic network.
	 * 
	 * @param analytic
	 */
	public AnalyticNode(Analytic analytic) {
	    this.analytic = analytic;
	    this.currentProcessSpan = new SpanImpl(SpanImpl.INITIAL);
	}
	
	/**
	 * Called by ancestors of this {@code Node} in the tree to set
	 * the {@link Span} of time modified by that ancestor's 
	 * internal processing class.
	 * 
	 * @param span				the {@link Span} of time processed.
	 * @param SeriesSubscriptions 	the List of {@link SeriesSubscription}s the ancestor node has processed.
	 * @return	
	 */
	@Override
	public void updateModifiedSpan(Span s, Subscription subscription) {
	    SpanImpl span = (SpanImpl)s;
		if(currentUpdateSpan.get() == null) {
			currentUpdateSpan.set(span);
		}
		
		synchronized(currentUpdates) {
		    boolean wasNewForScript = false;
		    if(currentUpdates.get(subscription) == null) {
		        wasNewForScript = true;
		        currentUpdates.put((SeriesSubscription)subscription, span);
	        }
		    
	        setUpdated(isUpdated() || wasNewForScript || span.extendsSpan(currentUpdates.get(subscription)));
	        if(isUpdated()) {
	        	currentUpdateSpan.getAndSet(currentUpdateSpan.get().union(span));
	        }
		}
	}

	/**
	 * Returns a flag indicating whether the implementing class has all of its expected input.
	 * 
	 * @return	a flag indicating whether the implementing class has all of its expected input.
	 */
	@Override
	public boolean hasAllAncestorUpdates() {
	    boolean hasAllUpdates = currentUpdates.values().size() == getInputSubscriptions().size();
	    synchronized(currentUpdates) {
	        for(SpanImpl s : currentUpdates.values()) {
	            hasAllUpdates &= s.extendsSpan(currentProcessSpan);
	        }
	    }
		return hasAllUpdates;
	}
	
	@Override
	public Span process() {
		synchronized(currentUpdates) {
			currentProcessSpan.setSpan(currentUpdateSpan.get());
		}
		return analytic.preProcess(currentProcessSpan);
	}
	
	/**
	 * Returns a List of {@link SeriesSubscriptions} that the underlying Node supplies as output.
	 * 
	 * @return	a List of output {@link SeriesSubscriptions}
	 */
	@Override
	public List<Subscription> getOutputSubscriptions() {
		return new ArrayList<Subscription>(outputKeyMap.values());
	}

	/**
	 * Returns a List of {@link SeriesSubscriptions} that the underlying Node expects as input.
	 * 
	 * @return	a List of expected input {@link SeriesSubscriptions}
	 */
	@Override
	public List<Subscription> getInputSubscriptions() {
		return new ArrayList<Subscription>(inputKeyMap.values());
	}
	
	/**
	 * Returns the output {@link TimeSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the output {@link TimeSeries}
	 */
	@SuppressWarnings("unchecked")
	public DataSeries<E> getOutputTimeSeries(Subscription subscription) {
		if(outputTimeSeries == null) {
			this.outputTimeSeries = (Map<SeriesSubscription, DataSeries<E>>)new DataSeries<E>(subscription.getTimeFrames()[0].getPeriod());
		}
		
		if(outputTimeSeries.get(subscription) == null) {
			outputTimeSeries.put((SeriesSubscription)subscription, new DataSeries<E>(subscription.getTimeFrames()[0].getPeriod()));
		}
		
		return (DataSeries<E>) this.outputTimeSeries.get(subscription);
	}
	
	/**
	 * Sets the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @param	the input {@link TimeSeries}
	 */
	void setInputTimeSeries(Subscription subscription, DataSeries<E> timeSeries) {
		inputTimeSeries.put((SeriesSubscription)subscription, timeSeries);
	}
	
	/**
	 * Returns the input {@link TimeSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the input {@link TimeSeries}
	 */
	public DataSeries<E> getInputTimeSeries(Subscription subscription) {
		return (DataSeries<E>) inputTimeSeries.get(subscription);
	}
	
	/**
	 * Returns a flag indicating whether this {@link Node} has an output which the specified
	 * {@link Subscription} information can be derived from.
	 * 
	 * @param	subscription	the Subscription which may or may not be derivable from one of 
	 * 							this Node's outputs.
	 * @return 	true if so, false if not.
	 */
	@Override
	public boolean isDerivableSource(Subscription subscription) {
	    for(SeriesSubscription sub : outputKeyMap.values()) {
			if(subscription.isDerivableFrom(sub)) {
		        return true;
		    } 
		}
		
		return false;
	}
	
	/**
     * Returns the  {@link Subscription} from which the specified Subscription is derivable.
     * 
     * @param subscription     the Subscription which can be derived from one of this {@code Node}'s outputs.
     * @return                 One of this Node's derivable outputs or null.
     */
    public Subscription getDerivableOutputSubscription(Subscription subscription) {
        return null;
    }

	@Override
	public AnalyticContainer.Category getCategory() {
		return null;
	}

	@Override
	public void addInputSubscription(String key, Subscription subscription) {
		inputKeyMap.put(key, (SeriesSubscription)subscription);
	}

	@Override
	public void addOutputSubscription(String key, Subscription subscription) {
		outputKeyMap.put(key, (SeriesSubscription)subscription);
	}

	@Override
	public Subscription getInputSubscription(String key) {
		return inputKeyMap.get(key);
	}

	@Override
	public Subscription getOutputSubscription(String key) {
		return outputKeyMap.get(key);
	}

	@Override
	public void valueUpdated(DateTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <E extends TimePoint> void setValue(DateTime time, E e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCalculation(DateTime time, String key, double value) {
		
	}

	@Override
	public void setRange(DateTime time, String key, double high, double low) {
		
	}

	@Override
	public void setArea(DateTime time, DateTime nextTime, String key,
			double high, double low, double nextHigh, double nextLow) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public TimeSeries<E> getInputTimeSeries(String key) {
		return inputTimeSeries.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataSeries<E> getOutputTimeSeries(String key) {
		return outputTimeSeries.get(key);
	}

	@Override
	public TimeFrame getInputTimeFrame(String key, int timeFrameIndex) {
		return inputKeyMap.get(key).getTimeFrames()[timeFrameIndex];
	}

	@Override
	public TimeFrame getOutputTimeFrame(String key, int timeFrameIndex) {
		return outputKeyMap.get(key).getTimeFrames()[timeFrameIndex];
	}
	
}
