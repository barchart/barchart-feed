package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import rx.subscriptions.Subscriptions;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Analytic;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.Subscription;

public class AnalyticNode extends Node {
	public enum SpanOperation { UNION, INTERSECTION };
	
	/** Holds the keys which describe the input mappings and are used to correlate the inputs with their {@link Subscription}s */
	private Map<String, Subscription> inputKeyMap = new ConcurrentHashMap<String, Subscription>();
	/** Holds the keys which describe the output mappings and are used to correlate the output with their {@link Subscription}s */
	private Map<String, Subscription> outputKeyMap = new ConcurrentHashMap<String, Subscription>();
	
	/** Contains all currently inputted {@link Subscriptions}, to be tested on each process cycle to see if all required inputs have been received */
	private Map<Subscription, Span> currentUpdates = Collections.synchronizedMap(new HashMap<Subscription, Span>());
	
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
	 * @param subscriptions 	the List of {@link Subscription}s the ancestor node has processed.
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
			currentUpdates.put(subscription, span);
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

	/**
	 * Starts the processing of the previously set {@link Span}
	 * @return	the updated Span
	 */
	@Override
	public Span process() {
		synchronized(currentUpdates) {
			currentProcessSpan = currentUpdateSpan.get();
			currentUpdates.clear();
		}
		return analytic.preProcess(currentProcessSpan);
	}

	/**
	 * Returns a List of {@link Subscriptions} that the underlying Node supplies as output.
	 * 
	 * @return	a List of output {@link Subscriptions}
	 */
	@Override
	public List<Subscription> getOutputSubscriptions() {
		return new ArrayList<Subscription>(outputKeyMap.values());
	}

	/**
	 * Returns a List of {@link Subscriptions} that the underlying Node expects as input.
	 * 
	 * @return	a List of expected input {@link Subscriptions}
	 */
	@Override
	public List<Subscription> getInputSubscriptions() {
		return new ArrayList<Subscription>(inputKeyMap.values());
	}

	/**
	 * Returns the output {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the output {@link TimeSeries}
	 */
	@Override
	public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription) {
		return null;
	}

	/**
	 * Returns the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the input {@link TimeSeries}
	 */
	@Override
	public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription) {
		return null;
	}

}
