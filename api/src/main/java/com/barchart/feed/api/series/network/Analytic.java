package com.barchart.feed.api.series.network;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Zone;
import com.barchart.feed.api.series.Range;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;

public interface Analytic {
    /**
     * Sets the input {@link DataSeries} corresponding to with the specified key
     * 
     * @param key      the String acting as key for the corresponding {@link DataSeries}
     * @param   the input {@link DataSeries}
     */
    public <E extends DataPoint> void addInputTimeSeries(String key, DataSeries<E> timeSeries);
    /**
     * Returns the input {@link DataSeries} corresponding to with the specified key
     * 
     * @param key        the String acting as key for the corresponding {@link DataSeries}
     * @return  the input {@link DataSeries}
     */
    public <E extends DataPoint> DataSeries<E> getInputTimeSeries(String key);
    /**
     * Sets the output {@link DataSeries} corresponding to with the specified key
     * 
     * @param key      the String acting as key for the corresponding {@link DataSeries}
     * @param   the input {@link DataSeries}
     */
    public <E extends DataPoint> void addOutputTimeSeries(String key, DataSeries<E> timeSeries);
	/**
	 * Returns the output {@link DataSeries}
	 * @param key  the mapping output key
	 * @return the output {@link DataSeries}
	 */
	public <E extends DataPoint> DataSeries<E> getOutputTimeSeries(String key);
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
	public <E extends DataPoint> void setValue(DateTime time, E e);
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
	 * Called to update the 2-dimensional {@link Zone} calculation for the specified key.
	 * 
	 * @param time		the time of the result.
	 * @param key		the identifier for the result.
	 * @param high		the high value of the range.
	 * @param low		the low value of the range.
	 */
	public void setZone(DateTime time, DateTime nextTime, String key, double high, double low, double nextHigh, double nextLow);
	
}
