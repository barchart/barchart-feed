package com.barchart.feed.openfeed.model;

import org.joda.time.DateTime;
import org.openfeed.AggregationPeriod;
import org.openfeed.MarketEntry;
import org.openfeed.MarketEntry.Descriptor;
import org.openfeed.MarketEntry.Type;
import org.openfeed.MarketHistoricalSnapshotOrBuilder;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public class MarketHistoricalStateBar extends MarketHistoricalState implements Bar, Comparable<Bar> {

	private int barCount = 1;
	private Period period;

	/**
	 * Construct a new wrapper with a mutable message builder.
	 */
	public MarketHistoricalStateBar() {
		super();
	}

	/**
	 * Construct a new wrapper with the given message or builder.
	 */
	public MarketHistoricalStateBar(final MarketHistoricalSnapshotOrBuilder message_) {
		super(message_);
	}

	@Override
	public Price getHigh() {
		return price(Type.HIGH);
	}

	@Override
	public Price getLow() {
		return price(Type.LOW);
	}

	@Override
	public DateTime getDate() {
		return timestamp();
	}

	@Override
	public Period getPeriod() {

		if (period == null) {

			switch (aggregation()) {
				case MINUTE:
					period = new Period(PeriodType.MINUTE, periodCount());
					break;
				case DAY:
					period = new Period(PeriodType.DAY, periodCount());
					break;
				case WEEK:
					period = new Period(PeriodType.WEEK, periodCount());
					break;
				case MONTH:
					period = new Period(PeriodType.MONTH, periodCount());
					break;
				case YEAR:
					period = new Period(PeriodType.YEAR, periodCount());
					break;
				default:
					period = Period.DAY;
			}

		}

		return period;

	}

	@Override
	public MarketHistoricalState aggregation(final AggregationPeriod ap) {
		period = null;
		return super.aggregation(ap);
	}

	@Override
	public MarketHistoricalState periodCount(final int count) {
		period = null;
		return super.periodCount(count);
	}

	@Override
	public InstrumentID getInstrument() {
		return new InstrumentID(String.valueOf(id()));
	}

	@Override
	public Price getOpen() {
		return price(Type.OPEN);
	}

	@Override
	public Price getClose() {
		return price(Type.CLOSE);
	}

	@Override
	public Size getLastSize() {
		return size(Type.CLOSE);
	}

	@Override
	public Price getBid() {
		return price(Type.BID);
	}

	@Override
	public Size getBidSize() {
		return size(Type.BID);
	}

	@Override
	public Price getAsk() {
		return price(Type.ASK);
	}

	@Override
	public Size getAskSize() {
		return size(Type.ASK);
	}

	@Override
	public Price getMidpoint() {
		return price(Type.MIDPOINT);
	}

	@Override
	public Size getVolume() {
		return avgSize(Type.VOLUME, barCount);
	}

	@Override
	public Size getVolumeUp() {
		return avgSize(Type.VOLUME_UP, barCount);
	}

	@Override
	public Size getVolumeDown() {
		return avgSize(Type.VOLUME_DOWN, barCount);
	}

	@Override
	public Price getTradedValue() {
		return avgPrice(Type.TRADED_VALUE, barCount);
	}

	@Override
	public Price getTradedValueUp() {
		return avgPrice(Type.TRADED_VALUE_UP, barCount);
	}

	@Override
	public Price getTradedValueDown() {
		return avgPrice(Type.TRADED_VALUE_DOWN, barCount);
	}

	@Override
	public Size getTradeCount() {
		return avgSize(Type.TRADES, barCount);
	}

	@Override
	public Size getOpenInterest() {
		return avgSize(Type.INTEREST, barCount);
	}

	@Override
	public <E extends Bar> void merge(final E other, final boolean advanceTime) {

		// Close and Volume should *always* have values, the rest may be null

		Price open = price(Type.OPEN);
		Price close = price(Type.CLOSE);
		Size openInterest = size(Type.INTEREST);

		Price otherHigh = other.getHigh();
		Price otherLow = other.getLow();
		final Size otherVolume = other.getVolume();
		final Price otherValue = other.getClose().mult(otherVolume);

		if (other.getPeriod().getPeriodType() == PeriodType.TICK) {
			otherHigh = otherLow = other.getClose();
		}

		try {

			final MarketStateEntry high = entry(Type.HIGH);

			if (high == null) {
				entry(new MarketStateEntry().type(MarketEntry.Type.HIGH).price(otherHigh));
			} else if (high.price().isNull() || (!otherHigh.isNull() && otherHigh.greaterThan(high.price()))) {
				high.price(otherHigh);
			}

			final MarketStateEntry low = entry(Type.LOW);

			if (low == null) {
				entry(new MarketStateEntry().type(MarketEntry.Type.LOW).price(otherLow));
			} else if (low.price().isNull() || (!otherLow.isNull() && otherLow.lessThan(low.price()))) {
				low.price(otherLow);
			}

		} catch (final ArithmeticException ae) {
			ae.printStackTrace();
		}

		add(Type.VOLUME, otherVolume);
		add(Type.TRADED_VALUE, otherValue);

		if (close.greaterThan(other.getClose())) {
			add(Type.TRADED_VALUE_DOWN, otherValue);
			add(Type.VOLUME_DOWN, otherVolume);
		} else if (close.lessThan(other.getClose())) {
			add(Type.TRADED_VALUE_UP, otherValue);
			add(Type.VOLUME_UP, otherVolume);
		}

		if (getDate().compareTo(other.getDate()) <= 0) {
			close = other.getClose();
			entryOrNew(Type.CLOSE).price(close).size(other.getLastSize());
		}

		if (open.isNull()) {
			open = other.getOpen();
			entryOrNew(MarketEntry.Type.OPEN).price(open);
		}

		entryOrNew(MarketEntry.Type.BID).price(other.getBid()).size(other.getBidSize());
		entryOrNew(MarketEntry.Type.ASK).price(other.getAsk()).size(other.getAskSize());
		add(MarketEntry.Type.TRADES, other.getTradeCount());

		if (!other.getOpenInterest().isNull()) {
			if (openInterest.isNull()) {
				openInterest = other.getOpenInterest();
				entryOrNew(MarketEntry.Type.INTEREST).size(openInterest);
			} else {
				add(MarketEntry.Type.INTEREST, openInterest);
			}
		}

		barCount++;

		if (advanceTime) {
			entryOrNew(Type.TIME).timestamp(other.getDate());
		}

	}

	private MarketStateEntry add(final MarketEntry.Type type, final Price price) {

		if (!price.isNull() && !price.isZero()) {

			final MarketStateEntry entry = entryOrNew(type);

			if (entry.price().isNull() || entry.price().isZero()) {
				entry.price(price);
			} else {
				entry.price().add(price);
			}

			return entry;

		}

		return entry(type);

	}

	private MarketStateEntry add(final MarketEntry.Type type, final Size size) {

		if (!size.isNull() && !size.isZero()) {

			final MarketStateEntry entry = entryOrNew(type);

			if (entry.size().isNull() || entry.size().isZero()) {
				entry.size(size);
			} else {
				entry.size().add(size);
			}

			return entry;

		}

		return entry(type);

	}

	private Price avgPrice(final MarketEntry.Type type, final int count) {

		final Price p = price(type);

		if (!p.isNull() && barCount > 1) {
			return p.div(count);
		}

		return p;

	}

	private Size avgSize(final MarketEntry.Type type, final int count) {

		final Size s = size(type);

		if (!s.isNull() && barCount > 1) {
			return s.div(count);
		}

		return s;

	}

	@Override
	public <E extends DataPoint> int compareTo(final E other) {
		return getPeriod().getPeriodType().compareAtResolution(getDate(), other.getDate());
	}

	@Override
	public int compareTo(Bar o) {
		int ret = this.getDate().compareTo(o.getDate());
		if (ret == 0) {
			ret = this.toString().compareTo(o.toString());
		}
		return ret;
	}

	@Override
	public Price getSettlement() {
		return price(Type.SETTLE);
	}

	@Override
	public DateTime getLastTradeDay() {
		return timestamp(Type.CLOSE);
	}

	@Override
	public Price getGreeks(GREEK_TYPE type) {
		switch (type) {
			case DELTA:
				return price(Type.COMPUTED, Descriptor.OPTION_DELTA);
			case GAMMA:
				return price(Type.COMPUTED, Descriptor.OPTION_GAMMA);
			case THETA:
				return price(Type.COMPUTED, Descriptor.OPTION_THETA);
			case VEGA:
				return price(Type.COMPUTED, Descriptor.OPTION_VEGA);
			case RHO:
				return price(Type.COMPUTED, Descriptor.OPTION_RHO);
			default:
				return Price.NULL;
		}
	}

	@Override
	public Price getHistoricalVolatility() {
		return price(Type.COMPUTED, Descriptor.OPTION_HISTORICAL_TWENTY_DAY_VOLATILITY);
	}

	@Override
	public Price getImpliedVolatility() {
		return price(Type.COMPUTED, Descriptor.OPTION_IMPLIED_VOLATILITY);
	}

	@Override
	public Price getTheoreticalPrice() {
		return price(Type.COMPUTED, Descriptor.OPTION_THEORETICAL_PRICE);
	}

}
