package com.barchart.feed.api.series.analytics;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Area;
import com.barchart.feed.api.series.Range;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;

public interface Analytic {
    /**
     * Sets the input {@link TimeSeries} corresponding to with the specified key
     * 
     * @param key      the String acting as key for the corresponding {@link TimeSeries}
     * @param   the input {@link TimeSeries}
     */
    public <E extends TimePoint> void addInputTimeSeries(String key, TimeSeries<E> timeSeries);
    /**
     * Returns the input {@link TimeSeries} corresponding to with the specified key
     * 
     * @param key        the String acting as key for the corresponding {@link TimeSeries}
     * @return  the input {@link TimeSeries}
     */
    public <E extends TimePoint> TimeSeries<E> getInputTimeSeries(String key);
    /**
     * Sets the output {@link TimeSeries} corresponding to with the specified key
     * 
     * @param key      the String acting as key for the corresponding {@link TimeSeries}
     * @param   the input {@link TimeSeries}
     */
    public <E extends TimePoint> void addOutputTimeSeries(String key, TimeSeries<E> timeSeries);
	/**
	 * Returns the output {@link TimeSeries}
	 * @param key  the mapping output key
	 * @return the output {@link TimeSeries}
	 */
	public <E extends TimePoint> TimeSeries<E> getOutputTimeSeries(String key);
	/**
	 * Called immediately following the call to {@link Analytic#preProcess(Span)}
	 * to do the main work of the executing body of code.
	 * 
	 * @param      span    the {@link Span} of time over which processing is requested.
	 * @return     the actual downstream processes should process.
	 */
	public Span process(Span span);
	/**
     * Called to notify observers of an internal change which should result in notification
     * but no data additions.
     */
    public void valueUpdated();
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
