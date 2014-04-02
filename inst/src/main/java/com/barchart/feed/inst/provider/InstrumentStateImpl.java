package com.barchart.feed.inst.provider;

import org.openfeed.InstrumentDefinition;

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
	public void process(final InstrumentDefinition value) {
		def = value;
		
		if(def.hasDisplayBase()) {
			dispFrac = factory.newFraction(def.getDisplayBase(), def.getDisplayExponent());
		}
		
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
