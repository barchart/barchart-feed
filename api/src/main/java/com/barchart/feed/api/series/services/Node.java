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
	/** Specifier used to identify IO nodes used for bar building operations */
	public static final String TYPE_IO = "IO";
	
	/** List of {@code Node}s which are updated when this {@code Node} has finished processing. */
	private ConcurrentLinkedQueue<Node> childNodes;
	
	/** Thread monitor object */
	private Object waitLock = new Object();
	
	/** Flag to control startup and shutdown barriers */
	private boolean isRunning;
	
	/** Flag to indicated that a valid date range has been updated among source {@link TimeSeries} */
	private boolean isUpdated;
	
	
	
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
	 * @return	
	 */
	public boolean setModifiedSpan(Span span, List<Subscription>  subscriptions) {
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
		
		return true;
	}
	
	/**
	 * Called to set a flag indicating that there is data to process.
	 * 
	 * @param isUpdated
	 */
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	
	/**
	 * Returns a flag indicated whether or not there is data to process.
	 * 
	 * @return	a flag indicated whether or not there is data to process.
	 */
	public boolean isUpdated() {
		return isUpdated;
	}
	
	/**
	 * Returns the 
	 * @return
	 */
	protected Object getLock() {
		return waitLock;
	}
	
	
	///////////////////////////////////////////////////
	//    ABSTRACT METHODS TO BE IMPLEMENTED BELOW   //
	///////////////////////////////////////////////////
	/**
	 * Compares the specified {@link Subscription} with this {@code Node}'s
	 * output Subscriptions:
	 * 1. If there is an exact match, this Node is returned.
	 * 2. If not, we test the specified Subscription to see if it is "derivable"
	 *    from any of this Node's outputs. If so, then repeat this process on
	 *    Nodes immediately preceding this Node in the hierarchy. If not, then
	 *    return null. 
	 * 3. If no Node which is an exact match is found but that Node has derivable
	 * 	  output, use a "new" Node which can derive the output needed and attach
	 * 	  it and return the new Node.
	 * 	  
	 * 
	 * @param subscription
	 * @return
	 */
	protected abstract Node lookup(Subscription subscription);
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Collects expected input from ancestor nodes and starts this {@code Node}'s
	 * processing if all the expected inputs are present and in good form.
	 * 
	 * @param span				the {@link Span} processed.
	 * @param subscription		the Subscription describing and identifying the specified input.
	 */
	protected abstract void updateModifiedSpan(Span span, Subscription subscription);
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Returns a flag indicating whether the implementing class has all of its expected input.
	 * 
	 * @return	a flag indicating whether the implementing class has all of its expected input.
	 */
	protected abstract boolean hasAllAncestorUpdates();
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Starts the processing of the previously set {@link Span}
	 * @return	the updated Span
	 */
	protected abstract Span process();
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Returns a List of {@link Subscriptions} that the underlying Node supplies as output.
	 * 
	 * @return	a List of output {@link Subscriptions}
	 */
	protected abstract List<Subscription> getOutputSubscriptions();
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Returns a List of {@link Subscriptions} that the underlying Node expects as input.
	 * 
	 * @return	a List of expected input {@link Subscriptions}
	 */
	protected abstract List<Subscription> getInputSubscriptions();
	/**
	 * Returns the output {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the output {@link TimeSeries}
	 */
	protected abstract <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription);
	
	/**
	 * Returns the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the input {@link TimeSeries}
	 */
	protected abstract <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription);
	
	
	
	/**
	 * Main {@link Thread} body
	 */
	@Override
	public void run() {
		while(isRunning) {
			if(isUpdated()) {
				setUpdated(false);
				if(hasAllAncestorUpdates()) {
					Span span = this.process();
					if(span != null) {
						List<Subscription> outputs = getOutputSubscriptions();
						for(Node nextNode : childNodes) {
							nextNode.setModifiedSpan(span, outputs);
						}
					}
				}
			}
			
			if(!isUpdated()) {
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
}
