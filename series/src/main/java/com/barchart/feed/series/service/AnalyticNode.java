package com.barchart.feed.series.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.series.DataPoint;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;

public class AnalyticNode extends Node<SeriesSubscription> {
	public enum SpanOperation { UNION, INTERSECTION };
	
	/** Holds the keys which describe the input mappings and are used to correlate the inputs with their {@link SeriesSubscription}s */
	private Map<SeriesSubscription, String> inputKeyMap = new ConcurrentHashMap<SeriesSubscription, String>();
	/** Holds the keys which describe the output mappings and are used to correlate the output with their {@link SeriesSubscription}s */
	private Map<String, SeriesSubscription> outputKeyMap = new ConcurrentHashMap<String, SeriesSubscription>();
	/** Holds reverse mapping of output subscriptions to keys */
	private Map<SeriesSubscription, String> outputSubscriptionKeyMap = new ConcurrentHashMap<SeriesSubscription, String>();
		
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
	public <T extends Span> void updateModifiedSpan(T s, SeriesSubscription subscription) {
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
		return analytic.process(currentProcessSpan);
	}
	
	/**
	 * Returns a List of {@link SeriesSubscriptions} that the underlying Node supplies as output.
	 * 
	 * @return	a List of output {@link SeriesSubscriptions}
	 */
	@Override
	public List<SeriesSubscription> getOutputSubscriptions() {
		return new ArrayList<SeriesSubscription>(outputKeyMap.values());
	}

	/**
	 * Returns a List of {@link SeriesSubscriptions} that the underlying Node expects as input.
	 * 
	 * @return	a List of expected input {@link SeriesSubscriptions}
	 */
	@Override
	public List<SeriesSubscription> getInputSubscriptions() {
		return new ArrayList<SeriesSubscription>(inputKeyMap.keySet());
	}
	
	/**
	 * Returns the output {@link TimeSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the output {@link TimeSeries}
	 */
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(SeriesSubscription subscription) {
	    String key = outputSubscriptionKeyMap.get(subscription);
        return getOutputTimeSeries(key, subscription);
	}
	
	/**
     * Returns the output {@link TimeSeries}
     * @param key  the mapping output key
     * @return the output {@link TimeSeries}
     */
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(String outputKey, SeriesSubscription subscription) {
	    @SuppressWarnings("unchecked")
        DataSeries<E> dataSeries = (DataSeries<E>)this.analytic.getOutputTimeSeries(outputKey);
	    if(dataSeries == null) {
	        dataSeries = new DataSeries<E>(subscription.getTimeFrame(0).getPeriod());
	    }
        return dataSeries;
    }
	
	/**
	 * Sets the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @param	the input {@link TimeSeries}
	 */
	public <E extends DataPoint> void addInputTimeSeries(SeriesSubscription subscription, DataSeries<E> timeSeries) {
		this.analytic.addInputTimeSeries(outputSubscriptionKeyMap.get(subscription), timeSeries);
	}
	
	/**
	 * Returns the input {@link TimeSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the input {@link TimeSeries}
	 */
	@SuppressWarnings("unchecked")
    public <E extends DataPoint> DataSeries<E> getInputTimeSeries(String key) {
		return (DataSeries<E>) this.analytic.getInputTimeSeries(key);
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
	public boolean isDerivableSource(SeriesSubscription subscription) {
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
    public SeriesSubscription getDerivableOutputSubscription(SeriesSubscription subscription) {
        return null;
    }

    /**
     * Adds an input {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
	public void addInputKeyMapping(String key, SeriesSubscription subscription) {
		inputKeyMap.put((SeriesSubscription)subscription, key);
	}

	/**
     * Adds an output {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
	public void addOutputKeyMapping(String key, SeriesSubscription subscription) {
		outputKeyMap.put(key, (SeriesSubscription)subscription);
		outputSubscriptionKeyMap.put(subscription, key);
	}

}
