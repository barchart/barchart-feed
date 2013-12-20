package com.barchart.feed.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Time;

public class SpanImpl extends DataPoint implements Span {
	private Time nextTime;
	private DateTime nextDate;
	
	public SpanImpl(Period period, Time t, Time next) {
		super(period, t);
		this.nextTime = next;
		this.nextDate = new DateTime(nextTime.millisecond());
	}
	
	public SpanImpl(SpanImpl other) {
		super(new Period(other.period.getPeriodType(), other.period.size()), ValueFactoryImpl.factory.newTime(other.date.getMillis()));
		this.nextDate = new DateTime(other.nextDate.getMillis());
		this.nextTime = ValueFactoryImpl.factory.newTime(nextDate.getMillis());
	}

	@Override
	public <E extends TimePoint> int compareTo(E other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time getNextTime() {
		return nextTime;
	}
	
	public void setNextTime(Time t) {
		this.nextTime = t;
		this.nextDate = new DateTime(nextTime.millisecond());
	}

	@Override
	public int getNextIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString() {
		return new StringBuilder().append(new DateTime(time.millisecond()).toString()).
			append("  --  ").append(new DateTime(nextTime.millisecond())).toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
			+ ((nextTime == null) ? 0 : nextTime.hashCode());
		result = prime * result
			+ ((time == null) ? 0 : time.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpanImpl other = (SpanImpl) obj;
		if (nextTime == null) {
			if (other.nextTime != null)
				return false;
		} else if (!period.getPeriodType().resolutionInstant(nextDate).equals(period.getPeriodType().resolutionInstant(other.nextDate)))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!period.getPeriodType().resolutionInstant(date).equals(period.getPeriodType().resolutionInstant(other.date)))
			return false;
		return true;
	}

	@Override
	public Span union(Span span) {
		Time start = null;
		Time end = null;
		start = this.time.millisecond() < span.getTime().millisecond() ?  this.time : span.getTime();
		end = this.nextTime.millisecond() < span.getNextTime().millisecond() ? span.getNextTime() : this.nextTime;
		
		return new SpanImpl(span.getPeriod(), start, end);
	}

	@Override
	public Span intersection(Span span) {
		if((!(span.getTime().millisecond() < nextTime.millisecond())) && 
			(!(time.millisecond() < span.getNextTime().millisecond()))) {
			return null;
		}
		Time start = null;
		Time end = null;
		start = this.time.millisecond() < span.getTime().millisecond() ?  span.getTime() : this.time;
		end = this.nextTime.millisecond() < span.getNextTime().millisecond() ? this.nextTime : span.getNextTime();
		
		return new SpanImpl(span.getPeriod(), start, end);
	}

}
