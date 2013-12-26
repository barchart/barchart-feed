package com.barchart.feed.api.series.analytics;

import java.util.List;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.service.AnalyticContainer;

public interface Analytic {
	/**
	 * Returns a list of this {@code Analytic}'s input keys.
	 * 
	 * @return	a list of this {@code Analytic}'s input keys.
	 */
	public List<String> getInputKeys();
	/**
	 * Returns a list of this {@code Analytic}'s output keys.
	 * 
	 * @return	a list of this {@code Analytic}'s output keys.
	 */
	public List<String> getOutputKeys();
	/**
	 * Signals this {@code Analytic} to begin processing the
	 * specified span. Provides the underlying {@link Analytic}
	 * a chance to pre-process the data prior to having {@link Analytic#process(Span)}
	 * called on it.
	 * 
	 * @param span	the span of time in the {@link TimeSeries} to process.
	 * @return the span processed.
	 */
	public Span preProcess(Span span);
	/**
	 * Called immediately following the call to {@link Analytic#preProcess(Span)}
	 * to do the main work of the executing body of code.
	 * 
	 * @param span
	 * @return
	 */
	public Span process(Span span);
	/**
	 * Provides the communication path back up the graph to notify the containing
	 * parent of this {@code Analytic}'s completion of processing and means by
	 * which the input and output mapping can be referenced underneath.
	 * 
	 * @param processor		this analytic's container.
	 */
	public void setAnalyticContainer(AnalyticContainer processor);
	
}
