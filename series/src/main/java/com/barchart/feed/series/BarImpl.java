package com.barchart.feed.series;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.Period;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.ValueFactory;

/**
 * Contains the bar data.
 */
public class BarImpl extends DataPointImpl implements Bar {

	private static final ValueFactory VALUES = ValueFactoryImpl.getInstance();

	private InstrumentID instrument;
	private Price open;
	private Price high;
	private Price low;
	private Price close;
	private Price midpoint;
	private Price bid;
	private Size bidSize;
	private Price ask;
	private Size askSize;
	private Size volume;
	private Size volumeUp;
	private Size volumeDown;
	private Price tradedValue;
	private Price tradedValueUp;
	private Price tradedValueDown;
	private Size tickCount;
	private Size openInterest;

	/**
	 * Instantiates a new {@code BarImpl}
	 *
	 * @param date the {@link Time} of this bar.
	 * @param period the {@link Period} interval and type of this bar.
	 * @param open the Open {@link Price} of this bar.
	 * @param high the High {@link Price} of this bar.
	 * @param low the Low {@link Price} of this bar.
	 * @param close the Close {@link Price} of this bar.
	 * @param volume the Volume {@link Size} of this bar.
	 * @param openInterest the Open Interest {@link Size} of this bar.
	 */
	public BarImpl(final InstrumentID instrument, final Time date, final Period period, final Price open,
			final Price high, final Price low, final Price close, final Size volume, final Size openInterest) {

		this(instrument, new DateTime(date.millisecond(), DateTimeZone.forTimeZone(date.zone())), period, open, high,
				low, close, volume, null, null, null, openInterest, null, null, null, null, null, null, null, null);

	}

	/**
	 * Instantiates a new {@code BarImpl}
	 *
	 * @param date the {@link DateTime} of this bar.
	 * @param period the Period interval and type of this bar.
	 * @param open the Open {@link Price} of this bar.
	 * @param high the High {@link Price} of this bar.
	 * @param low the Low {@link Price} of this bar.
	 * @param close the Close {@link Price} of this bar.
	 * @param volume the Volume {@link Size} of this bar.
	 * @param openInterest the Open Interest {@link Size} of this bar.
	 */
	public BarImpl(final InstrumentID instrument, final DateTime date, final Period period, final Price open,
			final Price high, final Price low, final Price close, final Size volume, final Size openInterest) {

		this(instrument, date, period, open, high, low, close, volume, null, null, null, openInterest, null, null,
				null, null,
				null, null, null, null);

	}

	/**
	 * Instantiates a new {@code BarImpl}
	 *
	 * @param date the {@link DateTime} of this bar.
	 * @param period the Period interval and type of this bar.
	 * @param open the Open {@link Price} of this bar.
	 * @param high the High {@link Price} of this bar.
	 * @param low the Low {@link Price} of this bar.
	 * @param close the Close {@link Price} of this bar.
	 * @param midpoint the midpoint {@link Price} of this bar.
	 * @param bid the bid {@link Price} at close of this bar.
	 * @param bidSize the bid {@link Size} at close of this bar.
	 * @param ask the ask {@link Price} at close of this bar.
	 * @param askSize the ask {@link Size} at close of this bar.
	 * @param volume the Volume {@link Size} of this bar.
	 * @param volumeUp the Volume traded up {@link Size} of this bar.
	 * @param volumeDown the Volume traded down {@link Size} of this bar.
	 * @param tradedValue the traded value {@link Price} of this bar.
	 * @param tradeValueUp the positive traded value {@link Price} of this bar.
	 * @param tradeValueDown the negative traded value {@link Price} of this
	 *            bar.
	 * @param tickCount the number of ticks contributing to this bar.
	 * @param openInterest the Open Interest {@link Size} of this bar.
	 */
	public BarImpl(final InstrumentID instrument, final DateTime date, final Period period, final Price open,
			final Price high, final Price low, final Price close, final Size volume, final Size volumeUp,
			final Size volumeDown, final Size tickCount, final Size openInterest, final Price midpoint,
			final Price bid, final Size bidSize, final Price ask, final Size askSize, final Price tradedValue,
			final Price tradedValueUp, final Price tradedValueDown) {

		super(period, date);

		this.instrument = instrument;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.bid = bid;
		this.bidSize = bidSize;
		this.ask = ask;
		this.askSize = askSize;
		this.midpoint = midpoint;
		this.volume = zeroIfNull(volume);
		this.volumeUp = zeroIfNull(volumeUp);
		this.volumeDown = zeroIfNull(volumeDown);
		this.tradedValue = zeroIfNull(tradedValue);
		this.tradedValueUp = zeroIfNull(tradedValueUp);
		this.tradedValueDown = zeroIfNull(tradedValueDown);
		this.tickCount = zeroIfNull(tickCount);
		this.openInterest = openInterest;

	}

	private Size zeroIfNull(final Size s) {
		if (s == null)
			return VALUES.newSize(0);
		return s;
	}

	private Price zeroIfNull(final Price p) {
		if (p == null)
			return VALUES.newPrice(0, 0);
		return p;
	}

	/**
	 * Copy constructor
	 *
	 * @param other
	 */
	public BarImpl(final BarImpl other) {

		this(other.instrument,
				other.date,
				other.period,
				other.open,
				other.high,
				other.low,
				other.close,
				other.volume,
				other.volumeUp,
				other.volumeDown,
				other.tickCount,
				other.openInterest,
				other.midpoint,
				other.bid,
				other.bidSize,
				other.ask,
				other.askSize,
				other.tradedValue,
				other.tradedValueUp,
				other.tradedValueDown);

	}

	@Override
	public InstrumentID getInstrument() {
		return instrument;
	}

	public void setInstrument(final InstrumentID id) {
		this.instrument = id;
	}

	@Override
	public Period getPeriod() {
		return period;
	}

	public void setPeriod(final Period p) {
		this.period = p;
	}

	@Override
	public Price getOpen() {
		return open;
	}

	public void setOpen(final Price open) {
		this.open = open;
	}

	@Override
	public Price getHigh() {
		return high;
	}

	public void setHigh(final Price high) {
		this.high = high;
	}

	@Override
	public Price getLow() {
		return low;
	}

	public void setLow(final Price low) {
		this.low = low;
	}

	@Override
	public Price getClose() {
		return close;
	}

	public void setClose(final Price close) {
		this.close = close;
	}

	@Override
	public Price getMidpoint() {
		return midpoint;
	}

	public void setMidpoint(final Price midpoint) {
		this.midpoint = midpoint;
	}

	@Override
	public Price getBid() {
		return bid;
	}

	public void setBid(final Price bid) {
		this.bid = bid;
	}

	@Override
	public Size getBidSize() {
		return bidSize;
	}

	public void setBidSize(final Size bidSize) {
		this.bidSize = bidSize;
	}

	@Override
	public Price getAsk() {
		return ask;
	}

	public void setAsk(final Price ask) {
		this.ask = ask;
	}

	@Override
	public Size getAskSize() {
		return askSize;
	}

	public void setAskSize(final Size askSize) {
		this.askSize = askSize;
	}

	@Override
	public Size getVolume() {
		return volume;
	}

	public void setVolume(final Size volume) {
		this.volume = volume;
	}

	@Override
	public Size getVolumeUp() {
		return volumeUp;
	}

	public void setVolumeUp(final Size size) {
		this.volumeUp = size;
	}

	@Override
	public Size getVolumeDown() {
		return volumeDown;
	}

	public void setVolumeDown(final Size size) {
		this.volumeDown = size;
	}

	@Override
	public Price getTradedValue() {
		return tradedValue;
	}

	public void setTradedValue(final Price tradedValue) {
		this.tradedValue = tradedValue;
	}

	@Override
	public Price getTradedValueUp() {
		return tradedValueUp;
	}

	public void setTradedValueUp(final Price tradedValueUp) {
		this.tradedValueUp = tradedValueUp;
	}

	@Override
	public Price getTradedValueDown() {
		return tradedValueDown;
	}

	public void setTradedValueDown(final Price tradedValueDown) {
		this.tradedValueDown = tradedValueDown;
	}

	@Override
	public Size getTickCount() {
		return tickCount;
	}

	public void setTickCount(final Size size) {
		this.tickCount = size;
	}

	@Override
	public Size getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(final Size openInterest) {
		this.openInterest = openInterest;
	}

	@Override
	public Time getTime() {
		return time;
	}

	public void setTime(final Time t) {
		this.time = t;
	}

	@Override
	public <E extends Bar> void merge(final E other, final boolean advanceTime) {

		try {
			if (high == null || other.getHigh().greaterThan(high)) {
				high = other.getHigh();
			}
			if (low == null || other.getLow().lessThan(low)) {
				low = other.getLow();
			}
		} catch (final ArithmeticException ae) {
			ae.printStackTrace();
		}

		final Price value = other.getClose().mult(other.getVolume());
		tradedValue = tradedValue.add(value);

		if (close.greaterThan(other.getClose())) {
			tradedValueDown = tradedValueDown.add(value);
		} else if (close.lessThan(other.getClose())) {
			tradedValueUp = tradedValueUp.add(value);
		}

		close = other.getClose();
		bid = other.getBid();
		bidSize = other.getBidSize();
		ask = other.getAsk();
		askSize = other.getAskSize();
		volume = volume.add(other.getVolume());
		tickCount = tickCount.add(1);

		// Average the open interest for multi-day bars
		if (other.getOpenInterest() != null) {
			if (openInterest != null) {
				openInterest = openInterest.add(other.getOpenInterest()).div(2);
			} else {
				openInterest = other.getOpenInterest();
			}
		}

		if (advanceTime) {
			time = other.getTime();
			date = new DateTime(time.millisecond());
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[Bar: ").append(date)
				.append(" o=").append(open.asDouble())
				.append(" h=").append(high.asDouble())
				.append(" l=").append(low.asDouble())
				.append(" c=").append(close.asDouble())
				.append(" v=").append((int) volume.asDouble())
				.append(" vup=").append((int) volumeUp.asDouble())
				.append(" vdwn=").append((int) volumeDown.asDouble())
				.append(" oi=").append((int) openInterest.asDouble())
				.append("]");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((high == null) ? 0 : high.hashCode());
		result = prime * result + ((low == null) ? 0 : low.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((volumeUp == null) ? 0 : volumeUp.hashCode());
		result = prime * result + ((volumeDown == null) ? 0 : volumeDown.hashCode());
		result = prime * result + ((tickCount == null) ? 0 : tickCount.hashCode());
		result = prime * result
				+ ((openInterest == null) ? 0 : openInterest.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((volume == null) ? 0 : volume.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BarImpl other = (BarImpl) obj;
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
		if (volumeUp == null) {
			if (other.volumeUp != null)
				return false;
		} else if (!volumeUp.equals(other.volumeUp))
			return false;
		if (volumeDown == null) {
			if (other.volumeDown != null)
				return false;
		} else if (!volumeDown.equals(other.volumeDown))
			return false;
		if (tickCount == null) {
			if (other.tickCount != null)
				return false;
		} else if (!tickCount.equals(other.tickCount))
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