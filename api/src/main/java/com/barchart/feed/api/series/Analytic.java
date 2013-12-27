package com.barchart.feed.api.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.service.AnalyticContainer;
import com.barchart.feed.api.series.service.Subscription;

public abstract class Analytic {
	/** Reference to the containing AnalyticNode */
	protected AnalyticContainer<?> parentNode;
	
	/**
	 * Returns an array of this {@code Analytic}'s input keys.
	 * 
	 * @return	an array of this {@code Analytic}'s input keys.
	 */
	public static String[] getInputKeys() { return null; }
	/**
	 * Returns an array of this {@code Analytic}'s output keys.
	 * 
	 * @return	an array of this {@code Analytic}'s output keys.
	 */
	public static String[] getOutputKeys() { return null; }
	/**
	 * Signals this {@code Analytic} to begin processing the
	 * specified span. Provides the underlying {@link Analytic}
	 * a chance to pre-process the data prior to having {@link Analytic#process(Span)}
	 * called on it.
	 * 
	 * @param span	the span of time in the {@link TimeSeries} to process.
	 * @return the span processed.
	 */
	public abstract Span preProcess(Span span);
	/**
	 * Called immediately following the call to {@link Analytic#preProcess(Span)}
	 * to do the main work of the executing body of code.
	 * 
	 * @param span
	 * @return
	 */
	public abstract Span process(Span span);
	/**
	 * Provides the communication path back up the graph to notify the containing
	 * parent of this {@code Analytic}'s completion of processing and means by
	 * which the input and output mapping can be referenced underneath.
	 * 
	 * @param container		this analytic's container.
	 */
	public <S extends Subscription> void setAnalyticContainer(AnalyticContainer<S> container) {
		this.parentNode = container;
	}
	/**
     * Called to notify observers of an internal change which should result in notification
     * but no data additions.
     */
    public void valueUpdated() {
    	
    }
    /**
	 * Called to update the current calculation with a formal series model object.
	 * 
	 * @param time		the time of the result.
	 * @param e			a result of the type {@link E}
	 */
	public <E extends TimePoint> void setValue(DateTime time, E e) {
		
	}
	/**
	 * Called to update the current calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param value		the result.
	 */
	public void setCalculation(DateTime time, String key, double value) {
		
	}
	/**
	 * Called to update the {@link Range} calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param high		the high value of the range.
	 * @param low		the low value of the range.
	 */
	public void setRange(DateTime time, String key, double high, double low) {
		
	}
	/**
	 * Called to update the 2-dimensional {@link Area} calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param high		the high value of the range.
	 * @param low		the low value of the range.
	 */
	public void setArea(DateTime time, DateTime nextTime, String key, double high, double low, double nextHigh, double nextLow) {
		
	}
	
}
