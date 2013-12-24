package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.NodeDescriptor;
import com.barchart.feed.api.series.services.Processor;
import com.barchart.feed.api.series.services.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataPoint;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;

public class BarBuilder<E extends DataBar> extends Node implements Processor {
    private SeriesSubscription inputSubscription;
    private SeriesSubscription outputSubscription;
    
    private static final String INPUT_KEY = "Input";
    private static final String OUTPUT_KEY = "Output";
    
    private DataSeries<DataBar> inputTimeSeries;
    private DataSeries<DataBar> outputTimeSeries;
    
    private SpanImpl inputSpan = new SpanImpl(SpanImpl.INITIAL);
    private SpanImpl workingSpan;
    
    private DataBar currentMergeBar;
    private DateTime workingTargetDate;
    
    private int aggregationCount = -1;
    
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
		setUpdated(span.extendsSpan(inputSpan));
		this.inputSpan = (SpanImpl)span;
		if(workingSpan == null) {
			workingSpan = new SpanImpl(inputSpan);
		}
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
	public void startUp() {
	    System.out.println("NODE: " + this + " starting...");
	    super.startUp();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public Span process() {
		System.out.println(this + " processing span: " + inputSpan);
		
		Period inputPeriod = inputSubscription.getTimeFrames()[0].getPeriod();
		Period outputPeriod = outputSubscription.getTimeFrames()[0].getPeriod();
		
		DataSeries<DataPoint> outputSeries = (DataSeries)getOutputTimeSeries(outputSubscription);
		DataSeries<DataPoint> inputSeries = (DataSeries)getInputTimeSeries(inputSubscription);
		int inputStartIdx = inputSeries.indexOf(inputSpan.getTime(), false);
		int inputLastIdx = inputSeries.indexOf(inputSpan.getNextTime(), false);
		
		if(inputPeriod == outputPeriod) {
		    for(int i = inputStartIdx;i <= inputLastIdx;i++) {
		        outputSeries.insertData(inputSeries.get(i));
		    }
		    return new SpanImpl((SpanImpl)inputSpan);
		}else if(inputPeriod.getPeriodType() == outputPeriod.getPeriodType()){ //Types are equal but output interval is > 1
			if(inputPeriod.size() > 1) {
				throw new IllegalStateException(
					"Can't build bars from Type with an Interval that's not 1. Input=" + 
						inputPeriod + ", output=" + outputPeriod);
			}
			
			if(currentMergeBar == null) {
				currentMergeBar = (DataBar)inputSeries.get(inputStartIdx); 
				workingTargetDate = outputSubscription.getTradingWeek().getNextSessionDate(currentMergeBar.getDate(), outputPeriod);
				currentMergeBar.setDate(workingTargetDate);
				this.workingSpan.setDate(currentMergeBar.getDate());
				this.workingSpan.setNextDate(currentMergeBar.getDate());
				
				outputSeries.add(currentMergeBar);
			}
			
			for(int i = inputStartIdx;i < inputLastIdx + 1;i++) {
				DataBar currentIdxBar = (DataBar)inputSeries.get(i);
				if(currentIdxBar.getDate().isAfter(workingTargetDate)) {
					workingTargetDate = getNextSessionDate(workingTargetDate, outputPeriod);
					currentMergeBar = new DataBar(currentIdxBar);
					currentMergeBar.setDate(workingTargetDate);
					this.workingSpan.setDate(currentMergeBar.getDate());
					this.workingSpan.setNextDate(currentMergeBar.getDate());
					
					outputSeries.add(currentMergeBar);
					
				}else{
					currentMergeBar.merge(currentIdxBar, false);
				}
				//fire onNext event
			}
		}else{ //Period types are not equal
			
		}
		
		return null;
	}
	
	private DateTime getNextSessionDate(DateTime dt, Period period) {
		return outputSubscription.getTradingWeek().getNextSessionDate(currentMergeBar.getDate(), period);
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
	public DataSeries<DataBar> getOutputTimeSeries(Subscription subscription) {
		if(outputTimeSeries == null) {
			this.outputTimeSeries = new DataSeries<DataBar>(subscription.getTimeFrames()[0].getPeriod());
		}
		return (DataSeries<DataBar>) this.outputTimeSeries;
	}

	/**
	 * Returns the input {@link TimeSeries}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataSeries<DataBar> getInputTimeSeries(Subscription subscription) {
		return (DataSeries<DataBar>) this.inputTimeSeries;
	}
	
	/**
	 * Sets the input {@link TimeSeries} corresponding to with the specified {@link Subscription}
	 * 
	 * @param subscription		the Subscription acting as key for the corresponding {@link TimeSeries}
	 * @param	the input {@link TimeSeries}
	 */
	void setInputTimeSeries(Subscription subscription, TimeSeries<DataBar> timeSeries) {
		this.inputTimeSeries = (DataSeries<DataBar>) timeSeries;
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

	@Override
    public Category getCategory() {
        return Category.BAR_BUILDER;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder(getCategory().toString()).append(": ").
            append(inputSubscription).append(" ---> ").append(outputSubscription);
        return sb.toString();
    }

}
