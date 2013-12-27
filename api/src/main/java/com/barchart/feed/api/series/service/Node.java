package com.barchart.feed.api.series.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import rx.subscriptions.Subscriptions;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimeSeries;

/**
 * Base class which participates in an interconnected graph of executable objects,
 * which expect a number of pre-configured inputs and deliver one or more pre-configured
 * outputs.
 * 
 * @author David Ray
 */
public abstract class Node<S extends Subscription> implements Runnable {
	/** List of {@code Node}s which are updated when this {@code Node} has finished processing. */
	protected ConcurrentLinkedQueue<Node<S>> childNodes;
	
	/** Parent path */
	protected List<Node<S>> parentNodes;
	
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
		childNodes = new ConcurrentLinkedQueue<Node<S>>();
		parentNodes = new ArrayList<Node<S>>();
	}
	
	/**
	 * Starts this {@code Node}'s processing.
	 */
	public void startUp() {
		if(!isRunning) {
			this.isRunning = true;
			(new Thread(this, this.toString())).start();
		}
		
		for(Node<S> n : parentNodes) {
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
	public boolean setModifiedSpan(Span span, List<S>  ancestorOutputSubscriptions) {
		for(S s : ancestorOutputSubscriptions) {
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
	public void addChildNode(Node<S> node) {
		childNodes.add(node);
	}
	
	/**
	 * Removes the specified {@link Node} from the list of this {@link Node}'s children.
	 * 
	 * @param node	the {@link Node} to remove.
	 */
	public void removeChildNode(Node<S> node) {
		childNodes.remove(node);
	}
	
	/**
	 * Returns a {@link List} view of the child {@link Node}s of this {@code Node}.
	 * <em>Note:</em> Modifications to the returned list have no effect on the internal
	 * structure of this {@link Node}.
	 * 
	 * @return		a {@link List} view of the child {@link Node}s of this {@code Node}.
	 */
	public List<Node<S>> getChildNodes() {
		List<Node<S>> nodes = new ArrayList<Node<S>>();
		for(Iterator<Node<S>> i = childNodes.iterator();i.hasNext();) {
			nodes.add(i.next());
		}
		return nodes;
	}
	
	/**
     * Allows the implementing class to add the specified child node which involves connecting the 
     * output specified by the {@link Subscription} to the specified output via input/output keys.
     * 
     * @param node
     * @param subscription
     */
    public void addParentNode(Node<S> node) {
    	synchronized(parentNodes) {
    		parentNodes.add(node);
    	}
    }
    
    /**
	 * Removes the specified {@link Node} from the list of this {@link Node}'s children.
	 * 
	 * @param node	the {@link Node} to remove.
	 */
	public void removeParentNode(Node<S> node) {
		synchronized(parentNodes) {
			childNodes.remove(node);
		}
	}
    
    /**
     * Returns the List of parent {@code Node}s.
     * @return  the List of parent {@code Node}s.
     */
    public List<Node<S>> getParentNodes() {
    	synchronized(parentNodes) {
    		return new ArrayList<Node<S>>(parentNodes);
    	}
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
	 * Returns a flag indicating whether this {@link Node} has an output which the specified
	 * {@link Subscription} information can be derived from.
	 * 
	 * @param	subscription	the Subscription which may or may not be derivable from one of 
	 * 							this Node's outputs.
	 * @return 	true if so, false if not.
	 */
	public abstract boolean isDerivableSource(S subscription);
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Collects expected input from ancestor nodes and starts this {@code Node}'s
	 * processing if all the expected inputs are present and in good form.
	 * 
	 * @param span				the {@link Span} processed.
	 * @param subscription		the Subscription describing and identifying the specified input.
	 */
	protected abstract <T extends Span> void updateModifiedSpan(T span, S subscription);
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
	public abstract List<S> getOutputSubscriptions();
	/**
	 * Implemented by the node type handling data expected by this {@code Node}
	 * 
	 * Returns a List of {@link Subscriptions} that the underlying Node expects as input.
	 * 
	 * @return	a List of expected input {@link Subscriptions}
	 */
	public abstract List<S> getInputSubscriptions();
	/**
	 * Returns the  {@link Subscription} from among the many possible node outputs this node may have -  
	 * from which the specified Subscription is derivable.
	 * 
	 * @param subscription     the Subscription which can be derived from one of this {@code Node}'s outputs.
	 * @return                 One of this Node's derivable outputs or null.
	 */
	public abstract S getDerivableOutputSubscription(S subscription);
	
	
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
						List<S> outputs = getOutputSubscriptions();
						for(Node<S> nextNode : childNodes) {
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