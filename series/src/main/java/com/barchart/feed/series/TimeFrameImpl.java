package com.barchart.feed.series;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.barchart.feed.api.series.TimeFrame;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;

/**
 * Basic time unit aggregation used during comparison of participants
 * in network composition or chaining of nodes.
 * 
 * @author David Ray
 */
public class TimeFrameImpl implements TimeFrame {
	/** Meta characteristics of this {@code TimeFrame} */
	private Period period;
	/** The start date */
	private DateTime startDate;
	/** The last date the series should contain */
	private DateTime endDate;
	
	/**
	 * Constructs a new {@code TimeFrame}
	 * 
	 * @param period
	 * @param startDate
	 */
	public TimeFrameImpl(Period period, DateTime startDate, DateTime endDate) {
		this.period = period;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Returns the meta characteristics of this {@link TimeFrameImpl}
	 * 
	 * @return	the meta characteristics of this {@link TimeFrameImpl}
	 */
	@Override
	public Period getPeriod() {
		return period;
	}

	/**
	 * Returns the start date.
	 * 
	 * @return	the start date.
	 */
	@Override
	public DateTime getStartDate() {
		return startDate;
	}
	
	/**
	 * The ending date of this {@code TimeFrame}
	 * 
	 * @return	ending date of this {@code TimeFrame}
	 */
	@Override
	public DateTime getEndDate() {
		return endDate;
	}
	
	/**
	 * Tests the "derivability" data defined by this {@code TimeFrame}
	 * to see if there is a chance of sharing between {@link Node}s 
	 * defined by both.
	 * 
	 * @param other		the frame being tested for source compatibility
	 * @return	true if compatible / derivable of false if not.
	 */
	@Override
	public boolean isDerivableFrom(TimeFrame tf) {
		TimeFrameImpl other = (TimeFrameImpl)tf;
		boolean retVal = false;
		
		PeriodType otherType = other.getPeriod().getPeriodType();
		PeriodType thisType = period.getPeriodType();
        int otherDuration = other.getPeriod().size();
        int thisDuration = getPeriod().size();
        
		if(equals(other)) {
			retVal = true;
		}else{
		    if(((otherType == thisType && otherType != PeriodType.WEEK && (otherDuration == thisDuration || (thisDuration % otherDuration == 0 && other.startDate.isEqual(startDate) && areEqualOrNull(other.endDate, endDate)))) || 
		        (otherType.isLowerThan(thisType) && otherType != PeriodType.WEEK  && otherDuration == 1))) {
		        retVal = true;
            }
    		
		    //Use the highest resolution type to create comparison resolution instances.
		    //(i.e. July being > May is more significant than 4:30 > 2:00)
		    PeriodType resolutionDeterminer = otherType.isLowerThan(thisType) ? thisType : otherType;
    		if(resolutionDeterminer.resolutionInstant(other.startDate).isAfter(resolutionDeterminer.resolutionInstant(startDate))) {
    			retVal = false;
    		}
    		
    		if((other.endDate != null && endDate == null) || ((other.endDate != null && endDate != null) && 
    		    resolutionDeterminer.resolutionInstant(other.endDate).isAfter(resolutionDeterminer.resolutionInstant(endDate)))) {
    		    retVal = false;
    		}
		}
		
		return retVal;
	}
	
	private boolean areEqualOrNull(DateTime otherDate, DateTime endDate2) {
	    return (otherDate == null) || (endDate2 != null && otherDate.isEqual(endDate2));
	}

	/**
	 * <em>All Dates modified</em> to enable comparison at the resolution of the given PeriodType.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : period.getPeriodType().resolutionInstant(endDate).hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : period.getPeriodType().resolutionInstant(startDate).hashCode());
		return result;
	}

	/**
	 * <em>All Dates modified</em> to enable comparison at the resolution of the given PeriodType.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeFrameImpl other = (TimeFrameImpl) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (period.getPeriodType().compareAtResolution(endDate, other.endDate) != 0)
			return false;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (period.getPeriodType().compareAtResolution(startDate, other.startDate) != 0)
			return false;
		return true;
	}
	
	public String toString() {
	    return new StringBuilder().append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm ").print(startDate)).
	        append(period).toString();
	}
}
