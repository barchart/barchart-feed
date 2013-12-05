package com.barchart.feed.api.series;

import java.util.Iterator;

import rx.Observer;
import rx.Subscription;

import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.util.value.api.Time;

/**
 * Ordered collection/series of sequential time-based {@link TimePoint}s.
 * Provides specialized methods to search and retrieve data 
 * within this series in an optimized manner.
 */
public interface TimeSeries<E extends TimePoint> {

	/**
	 * Returns the {@link Period} which defines the aggregation of
	 * time and data for every {@link TimePoint}.
	 * 
	 * @return this {@code TimeSeries}' {@link Period}
	 */
	public Period getPeriod();

	/**
	 * Returns the earliest date available in this time series.
	 * 
	 * @return the earliest date available in this time series.
	 */
	public Time getStart();

	/**
	 * Returns the most recent date available in this time series.
	 * 
	 * @return	the most recent date available in this time series
	 */
	public Time getEnd();
	
	/**
	 * Returns the first {@link TimePoint} in this series.
	 * 
	 * @return	the first {@link TimePoint} in this series.
	 */
	public E getFirst();
	
	/**
	 * Returns the last {@link TimePoint} in this series.
	 * 
	 * @return	the last {@link TimePoint} in this series.
	 */
	public E getLast();

	/**
	 * Returns the size of this {@link TimeSeries}
	 * 
	 * @return	the size of this {@link TimeSeries}
	 */
	public int size();

	/**
	 * Returns the {@link TimePoint} at the specified index.
	 * 
	 * @return the {@link TimePoint}
	 * @throws	IndexOutOfBoundsException  if the index is out of range (index < 0 || index >= size())
	 */
	public E get(int index);
	
	/**
	 * Returns the {@link TimePoint} correlated to the specified time or null.
	 * 
	 * <pre><b>
	 * NOTE: 	All time based comparisons are made at the same {@link PeriodType} resolution as the 
	 * 			PeriodType this series is configured with, ignoring other date fields.
	 * </b></pre>
	 * 
	 * @param 		time	the time for which the returned {@link TimePoint} is correlated.
	 * @return		the {@link TimePoint} correlated to the specified time or null
	 */
	public E forTime(Time time);

	/**
     * Executes a binary search returning the index of the {@link TimePoint}
     * containing the date specified, or the index of the previous TimePoint 
     * if the the specified TimePoint is not found. 
     * <p>
     * This method will always return a valid index. If the specified date
     * is before the first date within the first TimePoint, 0 will be returned.
     * Likewise, if the date is after the last date within the last TimePoint
     * in this series, the index of the last TimePoint will be returned.
     * <p>
     * <pre>
	 * NOTE: 	All time based comparisons are made at the same {@link PeriodType} resolution as the 
	 * 			PeriodType this series is configured with, ignoring other date fields.
	 * </pre>
     * <br><br>
     * <em><b>Warning: this method is not thread-safe.</b></em>
     * 
     * @param date          the date for which the location index is searched.
     * @param idxUpper      the upper bounds of the search.
     * @param idxLower      the lower bounds of the search.
     * @return              the index of the "proper location" whether the date
     *                      exists in this {@code TimeSeries} or not.
     */
	public int closestIndexOf(Time time, int idxLower, int idxUpper);
	
	/**
     * Performs an optimized search returning either the index of the proper location
     * of the specified date, the index of the matching date found, or -1. 
     * <p>
     * If the "exactOnly" flag is true, and the specified date is <em><b>not</b></em> found,
     * then -1 is returned. If the "exactOnly" flag is false, and the specified date is
     * <em><b>not</b></em> found, the index of the location of the date if it were to be
     * inserted into the series, is returned. Otherwise, the index of the found date is
     * returned.
     * <p> 
     * Additionally, if the high - low > 128, or the series size > 128, this method does an 
     * interpolated search for the starting mid point of the binary search, then limits the 
     * space of the search to a span of 64 indexes - thus limiting the binary search to a 
     * maximum of 6 searches regardless of the size of this {@code TimeSeries}.
     * <p>
     * <pre>
	 * NOTE: 	All time based comparisons are made at the same {@link PeriodType} resolution as the 
	 * 			PeriodType this series is configured with, ignoring other date fields.
	 * </pre>
     * <br><br>
     * <em><b>Warning: this method is not thread-safe.
     *  
     * @param time          the date for which the location index is searched.
     * @param exactOnly     flag determining whether -1 is returned when the specified
     *                      date is not found. True if -1 is to be returned, false if
     *                      the index of insertion is to be returned.
     * @return              the index of the "proper location" whether the date
     *                      exists in this {@code TimeSeries} or not, when the exactOnly
     *                      flag is false. If true, this method returns -1 if the date
     *                      is not found. Otherwise returns the index of the found date.
     */
    public int indexOf(Time time, boolean exactOnly);
    
    /**
     * Returns an {@link Iterator} to traverse the contents of this series from
     * oldest {@link TimePoint} to newest TimePoint.
     * 
     * @return	an {@link Iterator} to traverse the contents of this series.
     */
    public Iterator<E> iterator();

	/**
	 * Returns an {@link Iterator} which traverses this series newest/most recent first
	 * to oldest.
	 * 
	 * @return	an {@link Iterator} configured to traverse in reverse.
	 */
	public Iterator<E> reverseIterator();
	
	/**
     * Returns an array of type T filled with
     * {@link TimePoint}s.
     * 
     * @return  an array of type T filled with
     *          {@link DataPoint}s.
     */
    public E[] toArray();
    
    /**
	 * Returns an {@link Observable<E>} that will notify its {@link Observer} upon update 
	 * of this {@code TimeSeries}
	 * 
	 * @param 		query	The {@link Observer} subclass used to register interest in updates
	 * 						to this series.
	 * @return				
	 */
	public Subscription subscribe(Observer<E> observer);

}