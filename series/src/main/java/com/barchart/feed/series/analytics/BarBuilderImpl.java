package com.barchart.feed.series.analytics;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.analytics.BarBuilder;
import com.barchart.feed.api.series.service.AnalyticContainer;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataPoint;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.SpanImpl;

public class BarBuilderImpl implements Analytic, BarBuilder {
	private AnalyticContainer analyticNode;
	
	private static final String INPUT_KEY = "Input";
    private static final String OUTPUT_KEY = "Output";
    
    private List<String> inputs = new ArrayList<String>();
    private List<String> outputs = new ArrayList<String>();
    
    private DataBar currentMergeBar;
    
    private SpanImpl inputSpan;
    private SpanImpl workingSpan;
    
    private DateTime workingTargetDate;
    
    
    
	/**
	 * Instantiates a new {@code BarBuilder}
	 * 
	 * @param analytic
	 */
	public BarBuilderImpl() {
		inputs.add(INPUT_KEY);
		outputs.add(OUTPUT_KEY);
	}

	/**
	 * Signals this {@code Analytic} to begin processing the
	 * specified span. Provides the underlying {@link Analytic}
	 * a chance to pre-process the data prior to having {@link Analytic#process(Span)}
	 * called on it.
	 * 
	 * @param span	the span of time in the {@link TimeSeries} to process.
	 * @return the span processed.
	 */
	@Override
	public Span preProcess(Span span) {
		return process(span);
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
		
		Period inputPeriod = analyticNode.getInputTimeFrame(INPUT_KEY, 0).getPeriod();
		Period outputPeriod = analyticNode.getOutputTimeFrame(OUTPUT_KEY, 0).getPeriod();
		
		DataSeries<DataPoint> outputSeries = (DataSeries)analyticNode.getOutputTimeSeries(OUTPUT_KEY);
		DataSeries<DataPoint> inputSeries = (DataSeries)analyticNode.getInputTimeSeries(INPUT_KEY);
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
				workingTargetDate = analyticNode.getOutputSubscription(OUTPUT_KEY).getTradingWeek().
					getNextSessionDate(currentMergeBar.getDate(), outputPeriod);
				currentMergeBar.setDate(workingTargetDate);
				this.workingSpan.setDate(currentMergeBar.getDate());
				this.workingSpan.setNextDate(currentMergeBar.getDate());
				
				outputSeries.add(currentMergeBar);
			}
			
			for(int i = inputStartIdx;i < inputLastIdx;i++) {
				DataBar currentIdxBar = (DataBar)inputSeries.get(i);
				if(currentIdxBar.getDate().isAfter(workingTargetDate)) {
					workingTargetDate = analyticNode.getOutputSubscription(OUTPUT_KEY).
						getTradingWeek().getNextSessionDate(workingTargetDate, outputPeriod);
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
	
	/**
	 * Provides the communication path back up the graph to notify the containing
	 * parent of this {@code Analytic}'s completion of processing and means by
	 * which the input and output mapping can be referenced underneath.
	 * 
	 * @param processor		this analytic's container.
	 */
	@Override
	public void setAnalyticContainer(AnalyticContainer processor) {
		this.analyticNode = processor;
	}

	/**
	 * Returns a list of this {@code Analytic}'s input keys.
	 * 
	 * @return	a list of this {@code Analytic}'s input keys.
	 */
	public List<String> getInputKeys() {
		return inputs;
	}
	
	/**
	 * Returns a list of this {@code Analytic}'s output keys.
	 * 
	 * @return	a list of this {@code Analytic}'s output keys.
	 */
	public List<String> getOutputKeys() {
		return outputs;
	}
	
}
