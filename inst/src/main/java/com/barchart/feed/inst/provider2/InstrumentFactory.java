package com.barchart.feed.inst.provider2;

import org.openfeed.proto.inst.InstrumentDefinition;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.inst.participant.InstrumentState;

public final class InstrumentFactory {

	private InstrumentFactory() {
		
	}
	
	public static Instrument instrument(final InstrumentDefinition def) {
		return new InstrumentImpl(def);
	}
	
	public static InstrumentState instrumentState(final InstrumentDefinition def) {
		return new InstrumentStateImpl(def);
	}
	
}
