package com.barchart.feed.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Time;


/**
 * An abstract representation of any time-based market data related to a specific
 * point in time.
 * 
 * @author David Ray
 */
public abstract class DataPoint implements TimePoint {
	/** The period describing the type and units of time */
	protected Period period;
	/** The time index of this {@code DataPoint} */
	protected Time time;
	/** Immutable internal representation for efficiency (note: this value is immutable anyway)*/
	protected DateTime date; 
	
	
	
	/**
	 * Constructs a new {@code DataPoint}
	 * 
	 * @param period	the {@link Period}
	 * @param t			the {@link Time}
	 */
	protected DataPoint(Period period, Time t) {
		this.period = period;
		this.time = t;
		this.date = new DateTime(time.millisecond());
	}
	
	/**
	 * Returns the time index of this {@code DataPoint}
	 * @return	the time index of this {@code DataPoint}
	 */
	@Override
	public Time getTime() {
		return time;
	}
	
	/**
	 * Returns the {@link DateTime}
	 * 
	 * @return
	 */
	public DateTime getDate() {
		return date;
	}
	
	/**
	 * Sets the {@link DateTime} object
	 * @param dt
	 */
	public void setDate(DateTime dt) {
		this.date = dt;
		this.time = ValueFactoryImpl.factory.newTime(date.getMillis());
	}
	
	/**
	 * Returns the configured aggregation period.
	 * 
	 * @return period the configured aggregation period.
	 */
	@Override
	public Period getPeriod() {
		return period;
	}
	
	/**
	 * Compares this {@code DataPoint} to the argument returning an integer
	 * according to the {@link Comparable} contract. Additionally, the comparison
	 * made is AT THE RESOLUTION of this {@code DataPoint} as specified by the
	 * {@link Period} specified at time of construction. 
	 * 
	 * NOTE:
	 * The comparison made is according to THIS {@code DataPoint}'s {@code PeriodType}
	 * ONLY, thus disregarding the granularity of the {@code TemporalType} set on
	 * the argument {@code DataPoint}.
	 */
	@Override
	public <E extends TimePoint> int compareTo(E other) {
		return  period.getPeriodType().compareAtResolution(date, ((DataPoint)other).date);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + ((time == null) ? 
        	0 : period.getPeriodType().resolutionInstant(date).hashCode());
        result = prime * result + ((period == null) ? 
        	0 : period.hashCode());
        return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataPoint other = (DataPoint) obj;
        if (!period.equals(other.period))
            return false;
        //Both TemporalType and Date are guaranteed non-null.
        if (period.getPeriodType().compareAtResolution(date, other.date) != 0)
            return false;
        return true;
	}
}
