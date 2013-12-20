package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.NodeDescriptor;
import com.barchart.feed.api.series.services.Processor;
import com.barchart.feed.api.series.services.Subscription;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataSeries;

public class BarBuilder extends Node implements Processor {
    private SeriesSubscription inputSubscription;
    private SeriesSubscription outputSubscription;
    
    private static final String INPUT_KEY = "Input";
    private static final String OUTPUT_KEY = "Output";
    
    private TimeSeries<?> inputTimeSeries;
    private TimeSeries<?> outputTimeSeries;
    
    
    
    public BarBuilder(Subscription subscription) {
        this.outputSubscription = (SeriesSubscription)subscription;
    }
    
    /**
     * Called by ancestors of this {@code Node} in the tree to set
     * the {@link Span} of time modified by that ancestor's 
     * internal processing class.
     * 
     * @param span              the {@link Span} of time processed.
     * @param subscriptions     the List of {@link Subscription}s the ancestor node has processed.
     * @return  
     */
	@Override
	public void updateModifiedSpan(Span span, Subscription subscription) {
		
	}

	/**
     * Returns a flag indicating whether the implementing class has all of its expected input.
     * BarBuilders and their subclasses always return true here because they are guaranteed
     * to have one input and one output.
     * 
     * @return  a flag indicating whether the implementing class has all of its expected input.
     */
	@Override
	public boolean hasAllAncestorUpdates() {
		return true;
	}

	@Override
	public Span process() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addOutputSubscription(String key, Subscription subscription) {
	    this.outputSubscription = (SeriesSubscription)subscription;
	}
	
	@Override
    public void addInputSubscription(String key, Subscription subscription) {
        this.inputSubscription = (SeriesSubscription)subscription;
    }
	
	/**
     * Returns the input {@link Subscription} mapped to the specified key.
     * @param key  the mapping for the input Subscription
     * @return the Subscription corresponding to the specified key.
     */
    public Subscription getInputSubscription(String key) {
        return inputSubscription;
    }
    /**
     * Returns the {@link Subscription} corresponding to the specified key;
     * 
     * @param      key     the key mapped to the required output
     * @return             the required output
     */
    public Subscription getOutputSubscription(String key) {
        return outputSubscription;
    }
    
    @Override
	public List<Subscription> getOutputSubscriptions() {
	    List<Subscription> l = new ArrayList<Subscription>();
	    l.add(outputSubscription);
		return l;
	}

	@Override
	public List<Subscription> getInputSubscriptions() {
		if(outputSubscription == null) {
			throw new IllegalStateException("Node: BarBuilder has no output Subscription - can't create an input Subscription.");
		}
		
	    List<Subscription> l = new ArrayList<Subscription>();
	    if(inputSubscription == null) {
	    	inputSubscription = BarBuilderNodeDescriptor.getLowerSubscription(outputSubscription);
	    	if(outputSubscription.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.TICK) {
	    		inputSubscription = new SeriesSubscription(inputSubscription.getSymbol(), inputSubscription.getInstrument(), 
	    			new BarBuilderNodeDescriptor(NodeDescriptor.TYPE_ASSEMBLER), outputSubscription.getTimeFrames(), outputSubscription.getTradingWeek());
	    	}
	    }
        l.add(inputSubscription);
        return l;
	}

	/**
	 * Returns the output {@link TimeSeries}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(Subscription subscription) {
		if(outputTimeSeries == null) {
			this.outputTimeSeries = new DataSeries<DataBar>(subscription.getTimeFrames()[0].getPeriod());
		}
		return (TimeSeries<E>)this.outputTimeSeries;
	}

	/**
	 * Returns the input {@link TimeSeries}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(Subscription subscription) {
		return (TimeSeries<E>) this.inputTimeSeries;
	}
	
	/**
	 * Sets the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @param	the input {@link TimeSeries}
	 */
	<E extends TimePoint> void setInputTimeSeries(Subscription subscription, TimeSeries<E> timeSeries) {
		this.inputTimeSeries = timeSeries;
	}
	
	@Override
	public Node[] lookup(Subscription subscription) {
		return subscription.equals(outputSubscription) ? new Node[] { this, null } : 
		    subscription.isDerivableFrom(outputSubscription) ? new Node[] { null, this } : 
		        null;
	}
	
	/**
     * Returns the  {@link Subscription} from which the specified Subscription is derivable.
     * 
     * @param subscription     the Subscription which can be derived from one of this {@code Node}'s outputs.
     * @return                 One of this Node's derivable outputs or null.
     */
    public Subscription getDerivableOutputSubscription(Subscription subscription) {
        return subscription.isDerivableFrom(outputSubscription) ? outputSubscription : null;
    }
	
	/**
     * Returns the key for the {@link Subscription} that the specified subscription is derivable from.
     * 
     * @param subscription     the subscription for which to find the derivable subscription's key - amongst
     *                         this Node's output Subscriptions. 
     * @return                 the key for the Subscription from which the specified Subscription is derivable.
     */
	@Override
    public String getDerivableOutputKey(Subscription subscription) {
        return subscription.isDerivableFrom(outputSubscription) ? OUTPUT_KEY : null;
    }

	/**
	 * Adds a child node which will be woken up and made to process
	 * when it's parents release data.
	 * 
	 * @param 	node 		a child node.
	 */
    @Override
    public void addChildNode(Node node) {
        childNodes.add(node);
    }

    @Override
    public Category getCategory() {
        // TODO Auto-generated method stub
        return null;
    }

}
