package com.barchart.feed.api.series.service;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Area;
import com.barchart.feed.api.series.Range;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.analytics.BarBuilder;
import com.barchart.feed.api.series.temporal.TimeFrame;

/**
 * Base type for all {@link Analytic}s, {@link Assembler}s, and {@link BarBuilder}s.
 *  
 * @author David Ray
 *
 */
public interface AnalyticContainer {
    public enum Category { ASSEMBLER, BAR_BUILDER, ANALYTIC };
    
    /**
     * Returns the category nature of this {@code Processor}
     *  
     * @return  the category nature of this {@code Processor}
     */
    public Category getCategory();
    /**
     * Adds an input {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
    public void addInputSubscription(String key, Subscription subscription);
    /**
     * Adds an output {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
    public void addOutputSubscription(String key, Subscription subscription);
    /**
     * Returns the input {@link Subscription} mapped to the specified key.
     * @param key  the mapping for the input Subscription
     * @return the Subscription corresponding to the specified key.
     */
    public Subscription getInputSubscription(String key);
    /**
     * Returns the {@link Subscription} corresponding to the specified key;
     * 
     * @param      key     the key mapped to the required output
     * @return             the required output
     */
    public Subscription getOutputSubscription(String key);
    /**
     * Returns the {@link TimeSeries} mapped to the specified key.
     * 
     * @param key	the input key.
     * @return	the {@link TimeSeries} mapped to the specified key.
     */
    public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(String key);
    /**
     * Returns the {@link TimeSeries} mapped to the specified key.
     * 
     * @param key	the output key.
     * @return	the {@link TimeSeries} mapped to the specified key.
     */
    public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(String key);
    /**
     * Returns the input {@link TimeFrame} configured for the specified index for 
     * the {@link Subscription} who's key is the specified key.
     * 
     * @param timeFrameIndex
     * @return	the {@link TimeFrame} configured for the specified index.
     */
    public TimeFrame getInputTimeFrame(String key, int timeFrameIndex);
    /**
     * Returns the output {@link TimeFrame} configured for the specified index for 
     * the {@link Subscription} who's key is the specified key.
     * 
     * @param timeFrameIndex
     * @return	the {@link TimeFrame} configured for the specified index.
     */
    public TimeFrame getOutputTimeFrame(String key, int timeFrameIndex);
    /**
     * Called to notify observers of an internal change which should result in notification
     * but no data additions.
     * 
     * @param time
     */
    public void valueUpdated(DateTime time);
    /**
	 * Called to update the current calculation with a formal series model object.
	 * 
	 * @param time		the time of the result.
	 * @param e			a result of the type {@link E}
	 */
	public <E extends TimePoint> void setValue(DateTime time, E e);
	/**
	 * Called to update the current calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param value		the result.
	 */
	public void setCalculation(DateTime time, String key, double value);
	/**
	 * Called to update the {@link Range} calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param high		the high value of the range.
	 * @param low		the low value of the range.
	 */
	public void setRange(DateTime time, String key, double high, double low);
	/**
	 * Called to update the 2-dimensional {@link Area} calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param high		the high value of the range.
	 * @param low		the low value of the range.
	 */
	public void setArea(DateTime time, DateTime nextTime, String key, double high, double low, double nextHigh, double nextLow);
    
}
