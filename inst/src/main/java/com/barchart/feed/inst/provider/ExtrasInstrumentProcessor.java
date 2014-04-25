package com.barchart.feed.inst.provider;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.inst.participant.InstrumentState;

class ExtrasInstrumentProcessor extends ExtrasInstrument implements InstrumentState {

	private LoadState loadState;

	@Override
	public LoadState loadState() {
		return loadState;
	}

	@Override
	public void process(final Instrument inst) {

		// Update parent fields
		securityType(inst.securityType());
		liquidityType(inst.liquidityType());
		bookStructure(inst.bookStructure());
		maxBookDepth(inst.maxBookDepth());
		vendor(inst.vendor());
		symbol(inst.symbol());
		description(inst.description());
		CFICode(inst.CFICode());
		exchangeCode(inst.exchangeCode());
		tickSize(inst.tickSize());
		pointValue(inst.pointValue());
		priceFormat(inst.priceFormat());
		calendar(inst.calendar());

		loadState = LoadState.FULL;

	}

	@Override
	public void reset() {
		loadState = LoadState.NULL;
	}

}
