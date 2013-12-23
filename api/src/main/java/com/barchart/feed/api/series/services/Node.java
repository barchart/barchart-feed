package com.barchart.feed.api.series.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import rx.subscriptions.Subscriptions;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;

/**
 * Base class which participates in an interconnected graph of executable objects,
 * which expect a number of pre-configured inputs and deliver one or more pre-configured
 * outputs.
 * 
 * @author David Ray
 */
public abstract class Node implements Runnable {
	/** List of {@code Node}s which are updated when this {@code Node} has finished processing. */
	protected ConcurrentLinkedQueue<Node> childNodes;
	
	/** Parent path */
	protected List<Node> parentNodes;
	
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
		parentNodes = new ArrayList<Node>();
	}
	
	/**
	 * Starts this {@code Node}'s processing.
	 */
	public void startUp() {
		if(!isRunning) {
			this.isRunning = true;
			(new Thread(this, this.toString())).start();
		}
		
		for(Node n : parentNodes) {
		    n.startUp();
		}
	}
	
	/**
	 * Shuts down this {@code Node}.
	 */
	public void shutDown() {
	    this.isRunning = false;
	    try {
            synchronized(waitLock) {
                waitLock.notify();
            }
        }catch(Exception e) { 
            e.printStackTrace();
        }
	}
	
	/**
	 * Called by ancestors of this {@code Node} in the tree to set
	 * the {@link Span} of time modified by that {@code Node}'s 
	 * internal processing class.
	 * 
	 * @param span							the {@link Span} of time processed.
	 * @param ancestorOutputSubscriptions 	the List of {@link Subscription}s the ancestor node has processed.
	 * @return	
	 */
	public boolean setModifiedSpan(Span span, List<Subscription>  ancestorOutputSubscriptions) {
		for(Subscription s : ancestorOutputSubscriptions) {
		    if(getInputSubscriptions().contains(s)) {
		        updateModifiedSpan(span, s);
		    }
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
	 * Allows the implementing class to add the specified child node which involves connecting the 
	 * output specified by the {@link Subscription} to the specified output via input/output keys.
	 * 
	 * @param node
	 * @param subscription
	 */
	public void addChildNode(Node node) {
		childNodes.add(node);
	}
	
	/**
     * Allows the implementing class to add the specified child node which involves connecting the 
     * output specified by the {@link Subscription} to the specified output via input/output keys.
     * 
     * @param node
     * @param subscription
     */
    public void addParentNode(Node node) {
        parentNodes.add(node);
    }
    
    /**
     * Returns the List of parent {@code Node}s.
     * @return  the List of parent {@code Node}s.
     */
    public List<Node> getParentNodes() {
        return parentNodes;
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
	public abstract Node[] lookup(Subscription subscription);
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Collects expected input from ancestor nodes and starts this {@code Node}'s
	 * processing if all the expected inputs are present and in good form.
	 * 
	 * @param span				the {@link Span} processed.
	 * @param subscription		the Subscription describing and identifying the specified input.
	 */
	protected abstract <T extends Span, S extends Subscription> void updateModifiedSpan(T span, S subscription);
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
	 * @param span TODO
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
	public abstract List<Subscription> getOutputSubscriptions();
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Returns a List of {@link Subscriptions} that the underlying Node expects as input.
	 * 
	 * @return	a List of expected input {@link Subscriptions}
	 */
	public abstract List<Subscription> getInputSubscriptions();
	/**
	 * Returns the output {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the output {@link TimeSeries}
	 */
	public abstract <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription);
	/**
	 * Returns the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @return	the input {@link TimeSeries}
	 */
	protected abstract <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription);
	/**
	 * Returns the key for the {@link Subscription} that the specified subscription is derivable from.
	 * 
	 * @param subscription     the subscription for which to find the derivable subscription's key - amongst
	 *                         this Node's output Subscriptions. 
	 * @return                 the key for the Subscription from which the specified Subscription is derivable.
	 */
	public abstract String getDerivableOutputKey(Subscription subscription);
	/**
	 * Returns the  {@link Subscription} from which the specified Subscription is derivable.
	 * 
	 * @param subscription     the Subscription which can be derived from one of this {@code Node}'s outputs.
	 * @return                 One of this Node's derivable outputs or null.
	 */
	public abstract Subscription getDerivableOutputSubscription(Subscription subscription);
	
	
	/**
	 * Main {@link Thread} body
	 */
	@Override
	public void run() {
		while(isRunning) {
			if(isUpdated()) {
				System.out.println(this + " isUpdated");
				setUpdated(false);
				if(hasAllAncestorUpdates()) {
					System.out.println(this + " hasAllAncestorUpdates()");
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
						System.out.println(this + " waiting...");
						waitLock.wait();
						System.out.println(this + " waking up");
					}
				}catch(Exception e) { 
					e.printStackTrace();
				}
			}
		}
	}
}
