package com.barchart.feed.series.analytics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Area;
import com.barchart.feed.api.series.Range;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.series.DataSeries;

public abstract class AnalyticBase implements Analytic {
    /** Maps a given {@link TimeSeries} to a {@link Subscription} */
    private Map<String, TimeSeries<?>> inputTimeSeries = new ConcurrentHashMap<String, TimeSeries<?>>();
    /** Maps a given {@link TimeSeries} to a {@link Subscription} */
    private Map<String, TimeSeries<?>> outputTimeSeries = new ConcurrentHashMap<String, TimeSeries<?>>();

    
    
    /**
     * Returns an array of this {@code Analytic}'s input keys.
     * 
     * @return  an array of this {@code Analytic}'s input keys.
     */
    public static String[] getInputKeys() { return null; }
    /**
     * Returns an array of this {@code Analytic}'s output keys.
     * 
     * @return  an array of this {@code Analytic}'s output keys.
     */
    public static String[] getOutputKeys() { return null; }
    
    /**
     * Returns the output {@link TimeSeries}
     * @param key  the mapping output key
     * @return the output {@link TimeSeries}
     */
    @SuppressWarnings("unchecked")
    public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(String key) {
        return (TimeSeries<E>)outputTimeSeries.get(key);
    }
    
    /**
     * Adds a mapping for an output data series.
     * @param key
     * @param series
     */
    public <E extends TimePoint> void addOutputTimeSeries(String key, TimeSeries<E> series) {
        this.outputTimeSeries.put(key, (DataSeries<?>)series);
    }
    
    /**
     * Returns the output {@link TimeSeries}
     * @param key  the mapping output key
     * @return the output {@link TimeSeries}
     */
    @SuppressWarnings("unchecked")
    public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(String key) {
        return (TimeSeries<E>)inputTimeSeries.get(key);
    }
    
    /**
     * Adds a mapping for an input data series.
     * @param key
     * @param series
     */
    public <E extends TimePoint> void addInputTimeSeries(String key, TimeSeries<E> series) {
        this.inputTimeSeries.put(key, (DataSeries<?>)series);
    }
    
    /**
     * Called immediately following the call to {@link Analytic#preProcess(Span)}
     * to do the main work of the executing body of code.
     * 
     * @param      span    the {@link Span} of time over which processing is requested.
     * @return     the actual downstream processes should process.
     */
    public abstract Span process(Span span);
    /**
     * Called to notify observers of an internal change which should result in notification
     * but no data additions.
     */
    public void valueUpdated() {
        
    }
    /**
     * Called to update the current calculation with a formal series model object.
     * 
     * @param time      the time of the result.
     * @param e         a result of the type {@link E}
     */
    public <E extends TimePoint> void setValue(DateTime time, E e) {
        
    }
    /**
     * Called to update the current calculation for the specified key.
     * 
     * @param time      the time of the result.
     * @param key       the identifier for the result.
     * @param value     the result.
     */
    public void setCalculation(DateTime time, String key, double value) {
        
    }
    /**
     * Called to update the {@link Range} calculation for the specified key.
     * 
     * @param time      the time of the result.
     * @param key       the identifier for the result.
     * @param high      the high value of the range.
     * @param low       the low value of the range.
     */
    public void setRange(DateTime time, String key, double high, double low) {
        
    }
    /**
     * Called to update the 2-dimensional {@link Area} calculation for the specified key.
     * 
     * @param time      the time of the result.
     * @param key       the identifier for the result.
     * @param high      the high value of the range.
     * @param low       the low value of the range.
     */
    public void setArea(DateTime time, DateTime nextTime, String key, double high, double low, double nextHigh, double nextLow) {
        
    }
}
