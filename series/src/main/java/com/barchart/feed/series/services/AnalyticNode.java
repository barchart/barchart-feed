package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Analytic;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.Subscription;

public class AnalyticNode extends Node {
	public enum SpanOperation { UNION, INTERSECTION };
	
	/** Holds the keys which describe the input mappings and are used to correlate the inputs with their {@link SeriesSubscription}s */
	private Map<String, SeriesSubscription> inputKeyMap = new ConcurrentHashMap<String, SeriesSubscription>();
	/** Holds the keys which describe the output mappings and are used to correlate the output with their {@link SeriesSubscription}s */
	private Map<String, SeriesSubscription> outputKeyMap = new ConcurrentHashMap<String, SeriesSubscription>();
	/** Reverse SeriesSubscription map to lookup keys. */
	private Map<SeriesSubscription, String> outputSubMap = new ConcurrentHashMap<SeriesSubscription, String>();
	
	/** Maps a given {@link TimeSeries} to a {@link Subscription} */
	private Map<SeriesSubscription, TimeSeries<?>> inputTimeSeries = new ConcurrentHashMap<SeriesSubscription, TimeSeries<?>>();
	
	/** Contains all currently input {@link SeriesSubscriptions}, to be tested on each process cycle to see if all required inputs have been received */
	private Map<SeriesSubscription, Span> currentUpdates = Collections.synchronizedMap(new HashMap<SeriesSubscription, Span>());
	
	/** The currently updated {@link Span} for the current process cycle */
	private volatile AtomicReference<Span> currentUpdateSpan;
	
	/** Snapshot of the update Span used during processing */
	private Span currentProcessSpan;
	
	/** The specific analytic indicator type */
	private Analytic analytic;

	
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
	public void updateModifiedSpan(Span span, Subscription subscription) {
		if(currentUpdateSpan.get() == null) {
			currentUpdateSpan.set(span);
		}
		
		setUpdated(currentUpdates.get(subscription).getNextTime().compareTo(span.getNextTime()) < 1);
		currentUpdateSpan.set(currentUpdateSpan.get().union(span));
		synchronized(currentUpdates) {
			currentUpdates.put((SeriesSubscription)subscription, span);
		}
	}

	/**
	 * Returns a flag indicating whether the implementing class has all of its expected input.
	 * 
	 * @return	a flag indicating whether the implementing class has all of its expected input.
	 */
	@Override
	public boolean hasAllAncestorUpdates() {
		return currentUpdates.keySet().containsAll(getInputSubscriptions());
	}
	
	@Override
	public Span process() {
		synchronized(currentUpdates) {
			currentProcessSpan = currentUpdateSpan.get();
			currentUpdates.clear();
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
	@Override
	public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription) {
		return null;
	}
	
	/**
	 * Sets the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @param	the input {@link TimeSeries}
	 */
	<E extends TimePoint> void setInputTimeSeries(Subscription subscription, TimeSeries<E> timeSeries) {
		inputTimeSeries.put((SeriesSubscription)subscription, timeSeries);
	}
	
	/**
	 * Returns the input {@link TimeSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the input {@link TimeSeries}
	 */
	@Override
	public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription) {
		return null;
	}
	
	@Override
	public Node[] lookup(Subscription subscription) {
	    Node[] retVal = null;
		for(SeriesSubscription sub : outputKeyMap.values()) {
		    if(subscription.equals(sub)) {
		        return new Node[] { this, null };
		    }else if(retVal != null) {
		        continue;
		    }else if(subscription.isDerivableFrom(sub)) {
		        retVal = new Node[] { null, this };
		    }
		}
		
		return retVal;
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
	
	/**
     * Returns the key for the {@link SeriesSubscription} that the specified SeriesSubscription is derivable from.
     * 
     * @param SeriesSubscription     the SeriesSubscription for which to find the derivable SeriesSubscription's key - amongst
     *                         this Node's output SeriesSubscriptions. 
     * @return                 the key for the SeriesSubscription from which the specified SeriesSubscription is derivable.
     */
	@Override
    public String getDerivableOutputKey(Subscription subscription) {
        for(Subscription sub : getOutputSubscriptions()) {
            if(subscription.isDerivableFrom(sub)) {
                return outputSubMap.get(sub);
            }
        }
        return null;
    }

    @Override
    public void addChildNode(Node node) {
        // TODO Auto-generated method stub
        
    }

}
