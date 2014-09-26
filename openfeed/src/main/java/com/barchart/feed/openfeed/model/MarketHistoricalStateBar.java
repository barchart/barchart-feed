package com.barchart.feed.openfeed.model;

import org.joda.time.DateTime;
import org.openfeed.MarketEntry.Type;
import org.openfeed.MarketHistoricalSnapshotOrBuilder;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public class MarketHistoricalStateBar extends MarketHistoricalState implements Bar {

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
		return entry(Type.HIGH).price();
	}

	@Override
	public Price getLow() {
		return entry(Type.LOW).price();
	}

	@Override
	public DateTime getDate() {
		return entry(Type.TIME).timestamp();
	}

	@Override
	public Period getPeriod() {
		
		switch (aggregation()) {
			case MINUTE:
				return new Period(PeriodType.MINUTE, periodCount());
			case DAY:
				return new Period(PeriodType.DAY, periodCount());
			case WEEK:
				return new Period(PeriodType.WEEK, periodCount());
			case MONTH:
				return new Period(PeriodType.MONTH, periodCount());
			case YEAR:
				return new Period(PeriodType.YEAR, periodCount());
		}
		
		return Period.DAY;
		
	}

	@Override
	public InstrumentID getInstrument() {
		return new InstrumentID(String.valueOf(id()));
	}

	@Override
	public Price getOpen() {
		return entry(Type.OPEN).price();
	}

	@Override
	public Price getClose() {
		return entry(Type.CLOSE).price();
	}

	@Override
	public Size getLastSize() {
		return entry(Type.CLOSE).size();
	}

	@Override
	public Price getBid() {
		return entry(Type.BID).price();
	}

	@Override
	public Size getBidSize() {
		return entry(Type.BID).size();
	}

	public DateTime getBidTime() {
		return entry(Type.BID).timestamp();
	}

	@Override
	public Price getAsk() {
		return entry(Type.ASK).price();
	}

	@Override
	public Size getAskSize() {
		return entry(Type.ASK).size();
	}

	public DateTime getAskTime() {
		return entry(Type.ASK).timestamp();
	}

	@Override
	public Price getMidpoint() {
		return entry(Type.MIDPOINT).price();
	}

	@Override
	public Size getVolume() {
		return entry(Type.VOLUME).size();
	}

	@Override
	public Size getVolumeUp() {
		return entry(Type.VOLUME_UP).size();
	}

	@Override
	public Size getVolumeDown() {
		return entry(Type.VOLUME_DOWN).size();
	}

	@Override
	public Price getTradedValue() {
		return entry(Type.TRADED_VALUE).price();
	}

	@Override
	public Price getTradedValueUp() {
		return entry(Type.TRADED_VALUE_UP).price();
	}

	@Override
	public Price getTradedValueDown() {
		return entry(Type.TRADED_VALUE_DOWN).price();
	}

	@Override
	public Size getTradeCount() {
		return entry(Type.TRADES).size();
	}

	@Override
	public Size getOpenInterest() {
		return entry(Type.INTEREST).size();
	}

	@Override
	public <E extends Bar> void merge(E other, boolean advanceTime) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends DataPoint> int compareTo(E other) {
		// TODO
		throw new UnsupportedOperationException();
	}

}
