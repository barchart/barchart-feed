package com.barchart.feed.inst.api;

import com.barchart.feed.inst.provider.NullInstrument;

public final class InstrumentConst {
	
	private InstrumentConst() {
		
	}
	
	public static final Instrument NULL_INSTRUMENT = new NullInstrument();
	

}
