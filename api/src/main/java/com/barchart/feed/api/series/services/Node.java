package com.barchart.feed.api.series.services;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;

/**
 * Base class which participates in an interconnected graph of executable objects,
 * which expect a number of preconfigured inputs and deliver one or more preconfigured
 * outputs.
 * 
 * @author David Ray
 */
public abstract class Node implements Runnable {
	
	/** List of {@code Node}s which are updated when this {@code Node} has finished processing. */
	private ConcurrentLinkedQueue<Node> childNodes;
	
	/** Thread monitor object */
	private Object waitLock = new Object();
	
	/** Flag to control startup and shutdown barriers */
	private boolean isRunning;
	
	
	
	/**
	 * Constructs a new {@code Node}
	 */
	public Node() {
		childNodes = new ConcurrentLinkedQueue<Node>();
	}
	
	/**
	 * Starts this {@code Node}'s processing.
	 */
	public void startUp() {
		if(!isRunning) {
			this.isRunning = true;
			(new Thread(this)).start();
		}
	}
	
	/**
	 * Shuts down this {@code Node}.
	 */
	public void shutDown() {
		if(isRunning) {
			this.isRunning = false;
		}
	}
	
	/**
	 * Called by ancestors of this {@code Node} in the tree to set
	 * the {@link Span} of time modified by that {@code Node}'s 
	 * internal processing class.
	 * 
	 * @param span				the {@link Span} of time processed.
	 * @param subscriptions 	the List of {@link Subscription}s the ancestor node has processed.
	 */
	public void setModifiedSpan(Span span, List<Subscription>  subscriptions) {
		for(Subscription s : subscriptions) {
			updateModifiedSpan(span, s);
		}
		
		try {
			synchronized(waitLock) {
				waitLock.notify();
			}
		}catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	public abstract void updateModifiedSpan(Span span, Subscription subscription);
	
	public abstract boolean hasAllAncestorUpdates();
	
	public abstract Span process();
	
	public abstract List<Subscription> getOutputSubscriptions();
	
	public abstract List<Subscription> getInputSubscriptions();
	
	public abstract <E extends TimePoint> TimeSeries<E> getOutputDataSeries(Subscription subscription);
	
	public abstract <E extends TimePoint> TimeSeries<E> getInputDataSeries(Subscription subscription);
	
	/**
	 * Main {@link Thread} body
	 */
	@Override
	public void run() {
		while(isRunning) {
			if(hasAllAncestorUpdates()) {
				Span span = this.process();
				
				if(span != null) {
					List<Subscription> outputs = getOutputSubscriptions();
					for(Node nextNode : childNodes) {
						nextNode.setModifiedSpan(span, outputs);
					}
				}
			}
			
			try {
				synchronized(waitLock) {
					waitLock.wait();
				}
			}catch(Exception e) { 
				e.printStackTrace();
			}
		}
	}
}
