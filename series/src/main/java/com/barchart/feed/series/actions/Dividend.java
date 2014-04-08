package com.barchart.feed.series.actions;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.CorporateActionType;
import com.barchart.feed.series.BarImpl;
import com.barchart.util.value.api.Price;

public class Dividend extends AbstractCorporateAction {

	private final Price amount;

	public Dividend(final InstrumentID instrument_, final DateTime timestamp_, final Price amount_) {

		super(instrument_, timestamp_, CorporateActionType.DIVIDEND, "Dividend distribution of " + amount_ + "/share");

		amount = amount_;

	}

	public Dividend(final UUID id_, final InstrumentID instrument_, final DateTime timestamp_, final Price amount_) {

		super(id_, instrument_, timestamp_, CorporateActionType.DIVIDEND, "Dividend distribution of " + amount_
				+ "/share");

		amount = amount_;

	}

	public Price amount() {
		return amount;
	}

	@Override
	public Bar adjust(final Bar bar) {

		switch(bar.getPeriod().getPeriodType()) {

			case TICK:
			case MINUTE:
			case DAY:

				// Native aggregations

				// Day of dividend, adjust price
				// Compare millis instead of date to ignore time zone
				if (DateTimeComparator.getDateOnlyInstance()
						.compare(bar.getDate().getMillis(), timestamp().getMillis()) == 0) {
					final BarImpl b = bar instanceof BarImpl ? (BarImpl) bar : new BarImpl(bar);
					b.setOpen(b.getOpen().add(amount));
					b.setHigh(b.getHigh().add(amount));
					b.setLow(b.getLow().add(amount));
					b.setClose(b.getClose().add(amount));
					b.setMidpoint(b.getMidpoint().add(amount));
					return b;
				}

				// Else, return original bar
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

		if (that instanceof Dividend) {
			final Dividend div = (Dividend) that;
			return instrument().equals(div.instrument())
					&& timestamp().equals(div.timestamp())
					&& type().equals(div.type())
					&& amount().equals(div.amount());
		}

		return false;

	}

}
