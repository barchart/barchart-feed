package com.barchart.feed.api.series.service;

import com.barchart.feed.api.series.Analytic;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.temporal.TimeFrame;

/**
 * Base type for all {@link Analytic}s, {@link Assembler}s, and {@link BarBuilder}s.
 *  
 * @author David Ray
 *
 */
public interface AnalyticContainer<S extends Subscription> {
    /**
     * Adds an input {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
    public void addInputSubscription(String key, S subscription);
    /**
     * Adds an output {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
    public void addOutputSubscription(String key, S subscription);
    /**
     * Returns the input {@link Subscription} mapped to the specified key.
     * @param key  the mapping for the input Subscription
     * @return the Subscription corresponding to the specified key.
     */
    public S getInputSubscription(String key);
    /**
     * Returns the {@link Subscription} corresponding to the specified key;
     * 
     * @param      key     the key mapped to the required output
     * @return             the required output
     */
    public S getOutputSubscription(String key);
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
    
}
