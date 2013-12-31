package com.barchart.feed.series.analytics;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataPoint;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;
import com.barchart.feed.series.service.SeriesSubscription;

public class BarBuilder extends AnalyticBase {
	private static final String INPUT_KEY = "Input";
    private static final String OUTPUT_KEY = "Output";
    
    private static String[] inputs = new String[] { INPUT_KEY };
    private static String[] outputs = new String[] { OUTPUT_KEY };
    
    private DataBar currentMergeBar;
    
    private SpanImpl inputSpan;
    private SpanImpl workingSpan;
    
    private DateTime workingTargetDate;
    
    /** The {@link Subscription} used to determine the output {@link Period} information */
    private SeriesSubscription subscription;
    
    
    
	/**
	 * Instantiates a new {@code BarBuilder}. It is important that
	 * the {@link Subscription} passed in has only one {@link TimeFrame} since
	 * a BarBuilder's job is to produce one TimeFrame of data and only one.
	 * Therefore, this constructor will usually only be called from internal
	 * resources or Test classes which have this special knowledge, hence this
	 * constructor is package private.
	 * 
	 * @param s    the configured {@link SeriesSubscription}
	 */
	BarBuilder(SeriesSubscription s) {
	    this.subscription = s;
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
		System.out.println(this + " processing span: " + inputSpan);
		
		DataSeries<DataPoint> outputSeries = (DataSeries)getOutputTimeSeries(BarBuilder.OUTPUT_KEY);
		DataSeries<DataPoint> inputSeries = (DataSeries)getInputTimeSeries(BarBuilder.INPUT_KEY);
		int inputStartIdx = inputSeries.indexOf(inputSpan.getTime(), false);
		int inputLastIdx = inputSeries.indexOf(inputSpan.getNextTime(), false);
		
		Period inputPeriod = inputSeries.getPeriod();
        Period outputPeriod = outputSeries.getPeriod();
        
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
				workingTargetDate = subscription.getTradingWeek().
					getNextSessionDate(currentMergeBar.getDate(), outputPeriod);
				currentMergeBar.setDate(workingTargetDate);
				this.workingSpan.setDate(currentMergeBar.getDate());
				this.workingSpan.setNextDate(currentMergeBar.getDate());
				
				outputSeries.add(currentMergeBar);
			}
			
			for(int i = inputStartIdx;i < inputLastIdx;i++) {
				DataBar currentIdxBar = (DataBar)inputSeries.get(i);
				if(currentIdxBar.getDate().isAfter(workingTargetDate)) {
					workingTargetDate = subscription.
						getTradingWeek().getNextSessionDate(workingTargetDate, outputPeriod);
					currentMergeBar = new DataBar(currentIdxBar);
					currentMergeBar.setDate(workingTargetDate);
					this.workingSpan.setDate(currentMergeBar.getDate());
					this.workingSpan.setNextDate(currentMergeBar.getDate());
					
					outputSeries.add(currentMergeBar);
				}else{
					currentMergeBar.merge(currentIdxBar, false);
				}
			}
			
			//Notify of change
			valueUpdated();
		}else{ //Period types are not equal
			
		}
		
		return null;
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
