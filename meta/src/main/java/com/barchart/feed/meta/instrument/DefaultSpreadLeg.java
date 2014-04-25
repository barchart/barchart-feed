package com.barchart.feed.meta.instrument;

import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;

public class DefaultSpreadLeg implements SpreadLeg {

	private final InstrumentID instrument;
	private final int ratio;

	public DefaultSpreadLeg(final InstrumentID inst_, final int ratio_) {
		instrument = inst_;
		ratio = ratio_;
	}

	@Override
	public InstrumentID instrument() {
		return instrument;
	}

	@Override
	public int ratio() {
		return ratio;
	}

}
