package com.barchart.feed.series.analytics;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.series.BarImpl;
import com.barchart.feed.series.DataPointImpl;
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.network.AnalyticBase;
import com.barchart.feed.series.network.SeriesSubscription;

public class BarBuilder extends AnalyticBase {
    public static final String NAME = "BarBuilder";
	public static final String INPUT_KEY = "Input";
    public static final String OUTPUT_KEY = "Output";
    
    private static String[] inputs = new String[] { INPUT_KEY };
    private static String[] outputs = new String[] { OUTPUT_KEY };
    
    private BarImpl currentMergeBar;
    
    private SpanImpl inputSpan;
    private SpanImpl workingSpan;
    
    private DateTime workingTargetDate;
    
    /** The {@link Subscription} used to determine the output {@link Period} information */
    private SeriesSubscription subscription;
    
    
    
	/**
	 * Instantiates a new {@code BarBuilder}. It is important that
	 * the {@link Subscription} passed in has only one {@link TimeFrameImpl} since
	 * a BarBuilder's job is to produce one TimeFrame of data and only one.
	 * Therefore, this constructor will usually only be called from internal
	 * resources or Test classes which have this special knowledge, hence this
	 * constructor is package private.
	 * 
	 * @param s    the configured {@link SeriesSubscription}
	 */
	BarBuilder(SeriesSubscription s) {
	    this.subscription = s;
	    setName(s.toString());
	}
	
	/**
	 * Returns the {@link Subscription} used to obtain underlying {@link Period}
	 * information.
	 * 
	 * @return     the configured {@link Subscription}
	 */
	public SeriesSubscription getSubscription() { 
	    return subscription;
	}

	/**
	 * Called immediately following the call to {@link Analytic#preProcess(Span)}
	 * to do the main work of the executing body of code.
	 * 
	 * @param span
	 * @return
	 */
	@SuppressWarnings(value = { "unchecked", "rawtypes" })
	@Override
	public Span process(Span span) {
	    //System.out.println(this + " processing span: " + span);
		this.inputSpan = (SpanImpl)span;
		
		DataSeriesImpl<DataPointImpl> outputSeries = (DataSeriesImpl)getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		DataSeriesImpl<DataPointImpl> inputSeries = (DataSeriesImpl)getInputTimeSeries(BarBuilder.INPUT_KEY);
		int inputStartIdx = inputSeries.indexOf(inputSpan.getTime(), false);
		int inputLastIdx = inputSeries.indexOf(inputSpan.getNextTime(), false);
		
		Period inputPeriod = inputSeries.getPeriod();
        Period outputPeriod = outputSeries.getPeriod();
        
        boolean barCompleted = false;
        
        if(inputPeriod.equals(outputPeriod)) {
		    for(int i = inputStartIdx;i <= inputLastIdx;i++) {
		        outputSeries.insertData(inputSeries.get(i));
		    }
		    return new SpanImpl(inputSpan);
		}else{ 
			if(inputPeriod.size() > 1) {
				throw new IllegalStateException(
					"Can't build bars from Type with an Interval that's not 1. Input=" + 
						inputPeriod + ", output=" + outputPeriod);
			}
			
			if(subscription.getTimeFrame(0).getPeriod().equals(new Period(PeriodType.SECOND, 1))) {
			    System.out.println("debug");
			}
			
			boolean wasAddedDuringInit = false;
			if(currentMergeBar == null) {
			    wasAddedDuringInit = true;
				currentMergeBar = new BarImpl((BarImpl)inputSeries.get(inputStartIdx)); 
				workingTargetDate = subscription.getTradingWeek().getNextSessionDate(inputSpan.getDate(), outputPeriod);
				currentMergeBar.setDate(workingTargetDate);
				workingSpan = new SpanImpl(subscription.getTimeFrame(0).getPeriod(), inputSpan.getTime(), inputSpan.getNextTime());
				this.workingSpan.setNextDate(workingTargetDate);
				System.out.println("IS INIT: ADDING NEW BAR " + currentMergeBar + "   " + outputSeries.getPeriod() + "  :  span = " + this.workingSpan);
				outputSeries.add(currentMergeBar);
			}else{
				workingSpan.setDate(workingTargetDate);
			}
			
			for(int i = wasAddedDuringInit ? inputStartIdx + 1 : inputStartIdx;i <= inputLastIdx;i++) {
				BarImpl currentIdxBar = (BarImpl)inputSeries.get(i);
				if(currentIdxBar.getDate().isAfter(workingTargetDate)) {
					workingTargetDate = subscription.getTradingWeek().getNextSessionDate(workingTargetDate, outputPeriod);
					currentMergeBar = new BarImpl(currentIdxBar);
					currentMergeBar.setDate(workingTargetDate);
					this.workingSpan.setNextDate(workingTargetDate);
					System.out.println("IS AFTER: ADDING NEW BAR " + currentMergeBar + "   " + outputSeries.getPeriod() + "  :  span = " + this.workingSpan);
					outputSeries.add(currentMergeBar);
					barCompleted = true;
				}else{
				    currentMergeBar.merge(currentIdxBar, false);
				    if(workingSpan.getPeriod().equals(new Period(PeriodType.SECOND, 1))) {
				        System.out.println("\tNOT IS AFTER: MERGING NEW BAR " + currentMergeBar + "   " + outputSeries.getPeriod() + "  :  span = " + this.workingSpan);
				    }
				}
			}
			
			//Notify of change
			valueUpdated();
		}
        
		return barCompleted ? workingSpan : null;
	}
	
	/**
	 * Returns a list of this {@code Analytic}'s input keys.
	 * 
	 * @return	a list of this {@code Analytic}'s input keys.
	 */
	public static String[] getInputKeys() {
		return inputs;
	}
	
	/**
	 * Returns a list of this {@code Analytic}'s output keys.
	 * 
	 * @return	a list of this {@code Analytic}'s output keys.
	 */
	public static String[] getOutputKeys() {
		return outputs;
	}

}
