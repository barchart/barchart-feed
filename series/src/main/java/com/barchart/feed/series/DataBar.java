package com.barchart.feed.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;


/**
 * Contains the bar data.
 * 
 * @param <T>   core value-util type
 */
public class DataBar extends DataPoint implements Bar {
	private Price open;
	private Price high;
	private Price low;
	private Price close;
	private Size volume;
	private Size openInterest;
	
	/**
	 * Instantiates a new {@code DataBar}
	 * 
	 * @param date				the {@link Time} of this bar.
	 * @param period			the Period interval and type of this bar.
	 * @param open				the Open {@link Price} of this bar.
	 * @param high				the High {@link Price} of this bar.
	 * @param low				the Low {@link Price} of this bar.
	 * @param close				the Close {@link Price} of this bar.
	 * @param volume			the Volume {@link Size} of this bar.
	 * @param openInterest		the Open Interest {@link Size} of this bar.
	 */
	public DataBar(Time date, Period period, Price open, Price high, 
		Price low, Price close, Size volume, Size openInterest) {
		
		super(period, date);
		
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.openInterest = openInterest;
	}
	
	/**
	 * Instantiates a new {@code DataBar}
	 * 
	 * @param date				the {@link DateTime} of this bar.
	 * @param period			the Period interval and type of this bar.
	 * @param open				the Open {@link Price} of this bar.
	 * @param high				the High {@link Price} of this bar.
	 * @param low				the Low {@link Price} of this bar.
	 * @param close				the Close {@link Price} of this bar.
	 * @param volume			the Volume {@link Size} of this bar.
	 * @param openInterest		the Open Interest {@link Size} of this bar.
	 */
	public DataBar(DateTime date, Period period, Price open, Price high, 
		Price low, Price close, Size volume, Size openInterest) {
		
		super(period, ValueFactoryImpl.factory.newTime(date.getMillis(), date.getZone().getID()));
		
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.openInterest = openInterest;
	}
	
	/**
	 * Copy constructor
	 * @param other
	 */
	public DataBar(DataBar other) {
		super(new Period(other.period.getPeriodType(), other.period.size()), 
			ValueFactoryImpl.factory.newTime(other.date.getMillis(), other.date.getZone().getID()));
		
		this.open = ValueFactoryImpl.factory.newPrice(other.getOpen().asDouble());
		this.high = ValueFactoryImpl.factory.newPrice(other.getHigh().asDouble());
		this.low = ValueFactoryImpl.factory.newPrice(other.getLow().asDouble());
		this.close = ValueFactoryImpl.factory.newPrice(other.getClose().asDouble());
		this.volume = ValueFactoryImpl.factory.newSize(other.getVolume().mantissa(),
			other.getVolume().exponent());
		this.openInterest = ValueFactoryImpl.factory.newSize(
			other.getOpenInterest().mantissa(), other.getOpenInterest().exponent());
	}
	
	/**
	 * Returns the {@link Period} (aggregation interval and units) of this
	 * {@code TimePoint}
	 * 
	 * @return	the {@link Period}
	 */
	public Period getPeriod() {
		return period;
	}
	
	
	/**
	 * Sets the {@link Period} (aggregation interval and units) of this
	 * {@code TimePoint}
	 * 
	 * @param	p the {@link Period}
	 */
	public void setPeriod(Period p) {
		this.period = p;
	}
	
	/**
	 * Returns the open price
	 * @return the open price
	 */
	@Override
	public Price getOpen() {
		return open;
	}
	
	/**
	 * Sets the open price
	 * @param open	the open price
	 */
	public void setOpen(Price open) {
		this.open = open;
	}
	
	/**
	 * Returns the high price
	 * @return the high price
	 */
	@Override
	public Price getHigh() {
		return high;
	}
	
	/**
	 * Sets the high price.
	 * @param high	the high price.
	 */
	public void setHigh(Price high) {
		this.high = high;
	}

	/**
	 * Returns the low price
	 * @return the low price
	 */
	@Override
	public Price getLow() {
		return low;
	}
	
	/**
	 * Sets the low price.
	 * 
	 * @param low the low price
	 */
	public void setLow(Price low) {
		this.low = low;
	}

	/**
	 * Returns the close price
	 * @return the close price
	 */
	@Override
	public Price getClose() {
		return close;
	}
	
	/**
	 * Sets the close price
	 * @param close	the close price
	 */
	public void setClose(Price close) {
		this.close = close;
	}

	/**
	 * Returns the volume
	 * @return the volume
	 */
	@Override
	public Size getVolume() {
		return volume;
	}
	
	/**
	 * Sets the volume
	 * @param volume	 the volume
	 */
	public void setVolume(Size volume) {
		this.volume = volume;
	}

	/**
	 * Returns the open interest (futures only)
	 * @return the open interest
	 */
	@Override
	public Size getOpenInterest() {
		return openInterest;
	}
	
	/**
	 * Sets the open interest
	 * @param openInterest	the open interest
	 */
	public void setOpenInterest(Size openInterest) {
		this.openInterest = openInterest;
	}

	/**
	 * Returns the time index of this {@link Bar}
	 * @return the time index of this {@link Bar}
	 */
	@Override
	public Time getTime() {
		return time;
	}
	
	/**
	 * Sets the time index of this {@code DataBar}
	 * @param t	the time index
	 */
	public void setTime(Time t) {
		this.time = t;
	}
	
	/**
	 * Merges the specified <@link Bar> with this one, possibly updating any
	 * barrier elements (i.e. High, Low, etc) given the underlying type. Used for
	 * aggregating information based on {@link PeriodType}
	 * 
	 * Returns a boolean indicating whether this time point should be closed - refusing
	 * any subsequent merges. If this Bar should be closed, this method returns
	 * true, false if not.
	 *  
	 * @param other		the other Bar to merge.
	 * @param advanceTime	true if the time should also be merged, false if not
	 */
	@Override
	public <E extends Bar> void merge(E other, boolean advanceTime) {
		if(other.getHigh().greaterThan(high)) {
			high = other.getHigh();
		}
		if(other.getLow().lessThan(low)) {
			low = other.getLow();
		}
		close = other.getClose();
		volume = volume.add(other.getVolume());
		
		if(advanceTime) {
			time = other.getTime();
			date = new DateTime(time.millisecond());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Bar: ").append(date)
		.append(" o=").append(open.asDouble())
		.append(" h=").append(high.asDouble())
		.append(" l=").append(low.asDouble())
		.append(" c=").append(close.asDouble())
		.append(" v=").append((int)volume.asDouble())
		.append("]");
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((high == null) ? 0 : high.hashCode());
		result = prime * result + ((low == null) ? 0 : low.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result
				+ ((openInterest == null) ? 0 : openInterest.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((volume == null) ? 0 : volume.hashCode());
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
		DataBar other = (DataBar) obj;
		if (close == null) {
			if (other.close != null)
				return false;
		} else if (!close.equals(other.close))
			return false;
		if (high == null) {
			if (other.high != null)
				return false;
		} else if (!high.equals(other.high))
			return false;
		if (low == null) {
			if (other.low != null)
				return false;
		} else if (!low.equals(other.low))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (openInterest == null) {
			if (other.openInterest != null)
				return false;
		} else if (!openInterest.equals(other.openInterest))
			return false;
		if (!period.equals(other.period))
            return false;
        if (period.getPeriodType().compareAtResolution(date, other.date) != 0)
            return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (volume == null) {
			if (other.volume != null)
				return false;
		} else if (!volume.equals(other.volume))
			return false;
		return true;
	}
}