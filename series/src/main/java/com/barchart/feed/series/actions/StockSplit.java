package com.barchart.feed.series.actions;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.CorporateActionType;
import com.barchart.feed.series.BarImpl;

public class StockSplit extends AbstractCorporateAction {

	private final int before;
	private final int after;

	private final DateTimeComparator DAY_COMPARATOR = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth());

	public StockSplit(final InstrumentID instrument_, final DateTime timestamp_, final int before_, final int after_) {

		super(instrument_, timestamp_, CorporateActionType.STOCK_SPLIT,
				before_ + ":" + after_ + " stock split");

		before = before_;
		after = after_;

	}

	public int before() {
		return before;
	}

	public int after() {
		return after;
	}

	@Override
	public Bar adjust(final Bar bar) {

		switch (bar.getPeriod().getPeriodType()) {

			case TICK:
			case MINUTE:
			case DAY:

				// Native aggregations

				// If before day of split, adjust price
				if (DAY_COMPARATOR.compare(bar.getDate(), timestamp()) < 0) {
					final BarImpl b = bar instanceof BarImpl ? (BarImpl) bar : new BarImpl(bar);
					b.setOpen(b.getOpen().mult(after).div(before));
					b.setHigh(b.getHigh().mult(after).div(before));
					b.setLow(b.getLow().mult(after).div(before));
					b.setClose(b.getClose().mult(after).div(before));
					b.setMidpoint(b.getMidpoint().mult(after).div(before));
					return b;
				}

				return bar;

			default:

				// Composed aggregations

				// These are composed from one of the native aggregations above,
				// prices should be adjusted before this composition happens
				throw new IllegalArgumentException("Price adjustments can only be done on native aggregations");

		}

	}

	@Override
	public boolean equals(final Object that) {

		if (that instanceof StockSplit) {
			final StockSplit ss = (StockSplit) that;
			return instrument().equals(ss.instrument())
					&& timestamp().equals(ss.timestamp())
					&& type().equals(ss.type())
					&& before() == ss.before()
					&& after() == ss.after();
		}

		return false;

	}

}
