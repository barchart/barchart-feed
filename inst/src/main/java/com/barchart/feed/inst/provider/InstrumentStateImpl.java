package com.barchart.feed.inst.provider;

import org.openfeed.proto.inst.InstrumentDefinition;

import com.barchart.feed.inst.participant.InstrumentState;

class InstrumentStateImpl extends InstrumentImpl implements InstrumentState {

	InstrumentStateImpl(final InstrumentDefinition def) {
		super(def);
	}
	
	@Override 
	public State state() {
		return state;
	}
	
	@Override
	public void process(InstrumentDefinition value) {
		def = value;
		// TODO update State
	}

	@Override
	public InstrumentDefinition definition() {
		return def;
	}

	@Override
	public void reset() {
		def = null;
		state = State.NULL;
	}
	
}
