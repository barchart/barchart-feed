package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observer;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.NetworkNotification;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.api.series.network.NodeType;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.analytics.BarBuilder;

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
	
	public String getName() {
	    return analytic.getName();
	}
	
	/**
	 * Done only once for first input span for each input subscription
	 * @param sub      the subscription with the first update
	 * @param span     the first span update
	 */
	private void mapFirstAncestorUpdate(SeriesSubscription sub, SpanImpl span) {
	    AtomicReference<SpanImpl> ar = new AtomicReference<SpanImpl>();
        ar.set(new SpanImpl(span));
        inputSpans.put(sub, ar);
	}
	
	/**
     * Done only once for first input span for each input subscription
     * @param sub      the subscription with the first update
     * @param span     the first span update
     */
    private void mapPreviousAncestorUpdate(SeriesSubscription sub, SpanImpl span) {
        AtomicReference<SpanImpl> ar = new AtomicReference<SpanImpl>();
        ar.set(new SpanImpl(span));
        lastInputSpans.put(sub, ar);
    }
    
    /**
     * Save all keyed input spans to the previous map.
     */
    private void savePreviousAncestorUpdates() {
        for(SeriesSubscription sub : inputSpans.keySet()) {
            AtomicReference<SpanImpl> ar = null;
            if((ar = lastInputSpans.get(sub)) == null) {
                lastInputSpans.put(sub, ar = new AtomicReference<SpanImpl>());
                ar.set(new SpanImpl(inputSpans.get(sub).get()));
            }
            ar.get().setSpan(inputSpans.get(sub).get());
        }
    }
	
	/**
	 * Called by ancestors of this {@code Node} in the tree to set
	 * the {@link Span} of time modified by that ancestor's 
	 * internal processing class.
	 * 
	 * @param span				the {@link Span} of time processed.
	 * @param SeriesSubscriptions 	the List of {@link SeriesSubscription}s the ancestor node has processed.
	 */
//	@Override
//	public <T extends Span> void updateModifiedSpan(T s, SeriesSubscription subscription) {
//	    SpanImpl span = (SpanImpl)s;
//	    
//	    currentUpdateSpan.compareAndSet(null, span);
//		
//	    synchronized(currentUpdates) {
//		    boolean wasNewForScript = false;
//		    if(wasNewForScript = (currentUpdates.get(subscription) == null)) {
//		        currentUpdates.put(subscription, span);
//	        }
//		    
//	        setUpdated(isUpdated() || wasNewForScript || span.extendsSpan(currentUpdates.get(subscription)));
//	        if(isUpdated()) {
//	        	currentUpdateSpan.getAndSet(currentUpdateSpan.get().union(span));
//	        }
//		}
//	}
	
	Map<SeriesSubscription, AtomicReference<SpanImpl>> inputSpans = new HashMap<SeriesSubscription, AtomicReference<SpanImpl>>();
	Map<SeriesSubscription, AtomicReference<SpanImpl>> lastInputSpans = new HashMap<SeriesSubscription, AtomicReference<SpanImpl>>();
	@Override
    public <T extends Span> void updateModifiedSpan(T s, SeriesSubscription subscription) {
        SpanImpl span = (SpanImpl)s;
        System.out.println("updateModifiedSpan:  " + span);
        synchronized(inputSpans) {
            boolean wasNewForScript = false;
            if(wasNewForScript = (inputSpans.get(subscription) == null)) {
                mapFirstAncestorUpdate(subscription, span);
            }
            setUpdated(wasNewForScript || span.extendsSpan(inputSpans.get(subscription).get()));
            if(isUpdated()) {
                inputSpans.get(subscription).get().setSpan(span);
                currentProcessSpan.setSpan(currentProcessSpan.equals(SpanImpl.INITIAL) ? span : currentProcessSpan.union(span));
                System.out.println("currentProcessSpan = " + currentProcessSpan);
            }
        }
	}

	/**
	 * Returns a flag indicating whether the implementing class has all of its expected input.
	 * 
	 * @return	a flag indicating whether the implementing class has all of its expected input.
	 */
//	@Override
//	public boolean hasAllAncestorUpdates() {
//	    boolean hasAllUpdates = currentUpdates.values().size() == getInputSubscriptions().size();
//	    synchronized(currentUpdates) {
//	        for(SpanImpl s : currentUpdates.values()) {
//	            hasAllUpdates &= s.extendsSpan(currentProcessSpan);
//	        }
//	    }
//		return hasAllUpdates;
//	}
	
	@Override
    public boolean hasAllAncestorUpdates() {
        boolean hasAllUpdates = inputSpans.values().size() == getInputSubscriptions().size();
        synchronized(inputSpans) {
            for(SeriesSubscription sub : inputSpans.keySet()) {
                AtomicReference<SpanImpl> prevRef = lastInputSpans.get(sub);
                hasAllUpdates &= (prevRef == null || inputSpans.get(sub).get().extendsSpan(prevRef.get()));
            }
        }
        return hasAllUpdates;
    }
	
	@Override
	public Span process() {
	    //currentProcessSpan.setSpan(currentUpdateSpan.get());
		
		Span span = analytic.process(currentProcessSpan);
		savePreviousAncestorUpdates();
		currentProcessSpan.setTime(currentProcessSpan.getNextTime());
		NetworkNotificationImpl note = new NetworkNotificationImpl(analytic.getName(), span);
		for(Observer<NetworkNotification> obs : observers) {
		    System.out.println("ON NEXT CALLED");
		    obs.onNext(note);
		}
		return span;
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
		if(inputKeyMap.isEmpty() && BarBuilder.class.equals(analytic.getClass())) {
			makeInputSubscription(getOutputSubscriptions().get(0));
		}
		return new ArrayList<SeriesSubscription>(inputKeyMap.keySet());
	}
	
	private void makeInputSubscription(SeriesSubscription outputSubscription) {
		if(outputSubscription == null) {
			throw new IllegalStateException("Node: BarBuilder has no output Subscription - can't create an input Subscription.");
		}
		
	    SeriesSubscription inputSubscription = BarBuilderNodeDescriptor.getLowerSubscription(outputSubscription);
    	if(outputSubscription.getTimeFrames()[0].getPeriod().getPeriodType() == PeriodType.TICK) {
    		inputSubscription = new SeriesSubscription(inputSubscription.getSymbol(), inputSubscription.getInstrument(), 
    			NodeType.ASSEMBLER.toString(), outputSubscription.getTimeFrames(), outputSubscription.getTradingWeek());
    	}
    	inputKeyMap.put(inputSubscription, BarBuilder.INPUT_KEY);
    }
	
	/**
	 * Returns the output {@link DataSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link DataSeries}
	 * @return	the output {@link DataSeries}
	 */
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(SeriesSubscription subscription) {
	    String key = outputSubscriptionKeyMap.get(subscription);
	    if(key == null) {
	    	StringBuilder error = new StringBuilder("Could not find key for subscription: ").append(subscription.toString()).append("\n");
	    		for(Map.Entry<SeriesSubscription, String> e : outputSubscriptionKeyMap.entrySet()) {
	    			error.append("\t").append(e.toString()).append("\n");
	    		}
	    			
	    	throw new IllegalArgumentException(error.toString());
	    }
        return getOutputTimeSeries(key, subscription);
	}
	
	/**
     * Returns the output {@link DataSeries}
     * @param key  the mapping output key
     * @return the output {@link DataSeries}
     */
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(String outputKey, SeriesSubscription subscription) {
	    DataSeries<E> dataSeries = this.analytic.getOutputTimeSeries(outputKey);
	    if(dataSeries == null) {
	        dataSeries = new DataSeriesImpl<E>(subscription.getTimeFrame(0).getPeriod());
	        this.analytic.addOutputTimeSeries(outputKey, dataSeries);
	    }
        return dataSeries;
    }
	
	/**
	 * Used for testing only... Users should use the other methods for retrieving
	 * a {@link DataSeries} so that one is created if it is not found.
	 * 
	 * @param outputKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(String outputKey) {
		return (DataSeries<E>)this.analytic.getOutputTimeSeries(outputKey);
	}
	
	/**
	 * Sets the input {@link DataSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link DataSeries}
	 * @param	the input {@link DataSeries}
	 */
	public <E extends DataPoint> void addInputTimeSeries(SeriesSubscription subscription, DataSeries<E> timeSeries) {
		this.analytic.addInputTimeSeries(inputKeyMap.get(subscription), timeSeries);
	}
	
	/**
	 * Returns the input {@link DataSeries} corresponding to with the specified {@link SeriesSubscription}
	 * 
	 * @param SeriesSubscription		the SeriesSubscription acting as key for the corresponding {@link DataSeries}
	 * @return	the input {@link DataSeries}
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
        for(SeriesSubscription s : outputKeyMap.values()) {
            if(subscription.isDerivableFrom(s)) {
                return s;
            }
        }
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
