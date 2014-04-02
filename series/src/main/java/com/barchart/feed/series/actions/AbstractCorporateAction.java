package com.barchart.feed.series.actions;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.CorporateAction;
import com.barchart.feed.api.series.CorporateActionType;

public abstract class AbstractCorporateAction implements CorporateAction {

	private final InstrumentID instrument;
	private final DateTime timestamp;
	private final CorporateActionType type;
	private final String description;

	protected AbstractCorporateAction(final InstrumentID instrument_, final DateTime timestamp_,
			final CorporateActionType type_, final String description_) {
		instrument = instrument_;
		timestamp = timestamp_;
		type = type_;
		description = description_;
	}

	@Override
	public InstrumentID instrument() {
		return instrument;
	}

	@Override
	public DateTime timestamp() {
		return timestamp;
	}

	@Override
	public CorporateActionType type() {
		return type;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public abstract Bar adjust(final Bar bar);

	@Override
	public abstract boolean equals(final Object that);

}
