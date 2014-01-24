package com.barchart.feed.series.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Zone;
import com.barchart.feed.api.series.Range;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.series.DataSeriesImpl;

public abstract class AnalyticBase implements Analytic {
    /** Maps a given {@link DataSeries} to a {@link Subscription} */
    private Map<String, DataSeries<?>> inputTimeSeries = new ConcurrentHashMap<String, DataSeries<?>>();
    /** Maps a given {@link DataSeries} to a {@link Subscription} */
    private Map<String, DataSeries<?>> outputTimeSeries = new ConcurrentHashMap<String, DataSeries<?>>();
    /** Node name */
    private String name;
    
    /**
     * Sets this analytic's node name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns this analytic's node name
     * @return  the name of this analytic's node
     */
    public String getName() {
        return name;
    }
    
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
     * Returns the output {@link DataSeries}
     * @param key  the mapping output key
     * @return the output {@link DataSeries}
     */
    @SuppressWarnings("unchecked")
    public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(String key) {
        return (DataSeries<E>)outputTimeSeries.get(key);
    }
    
    /**
     * Adds a mapping for an output data series.
     * @param key
     * @param series
     */
    public <E extends DataPoint> void addOutputTimeSeries(String key, DataSeries<E> series) {
        this.outputTimeSeries.put(key, (DataSeriesImpl<?>)series);
    }
    
    /**
     * Returns the output {@link DataSeries}
     * @param key  the mapping output key
     * @return the output {@link DataSeries}
     */
    @SuppressWarnings("unchecked")
    public <E extends DataPoint> DataSeries<E> getInputTimeSeries(String key) {
        return (DataSeries<E>)inputTimeSeries.get(key);
    }
    
    /**
     * Adds a mapping for an input data series.
     * @param key
     * @param series
     */
    public <E extends DataPoint> void addInputTimeSeries(String key, DataSeries<E> series) {
        this.inputTimeSeries.put(key, (DataSeriesImpl<?>)series);
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
     * @param key       the key to the series having the value set.
     * @param e         a result of the type {@link E}
     */
    public <E extends DataPoint> void setValue(DateTime time, String key, E e) {
        
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
     * Called to update the 2-dimensional {@link Zone} calculation for the specified key.
     * 
     * @param time      the time of the result.
     * @param key       the identifier for the result.
     * @param high      the high value of the range.
     * @param low       the low value of the range.
     */
    public void setZone(DateTime time, DateTime nextTime, String key, double high, double low, double nextHigh, double nextLow) {
        
    }
}
