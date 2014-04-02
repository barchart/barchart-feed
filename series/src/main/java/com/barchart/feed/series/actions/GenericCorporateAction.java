package com.barchart.feed.series.actions;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.Bar;
import com.barchart.feed.api.series.CorporateAction;
import com.barchart.feed.api.series.CorporateActionType;

public class GenericCorporateAction extends AbstractCorporateAction {

	public GenericCorporateAction(final InstrumentID instrument_, final DateTime timestamp_,
			final CorporateActionType type_,
			final String description_) {
		super(instrument_, timestamp_, type_, description_);
	}

	@Override
	public Bar adjust(final Bar bar) {
		return bar;
	}

	@Override
	public boolean equals(final Object that) {

		if (that instanceof CorporateAction) {
			final CorporateAction action = (CorporateAction) that;
			return instrument().equals(action.instrument())
					&& timestamp().equals(action.timestamp())
					&& type().equals(action.type())
					&& description().equals(action.description());
		}

		return false;

	}

}
