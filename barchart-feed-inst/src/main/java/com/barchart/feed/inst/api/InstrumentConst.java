package com.barchart.feed.inst.api;

import com.barchart.feed.inst.enums.MarketCurrency;
import com.barchart.feed.inst.provider.NullInstrument;
import com.barchart.feed.inst.provider.NullInstrumentGUID;

public final class InstrumentConst {
	
	private InstrumentConst() {
		
	}
	
	public static final Instrument NULL_INSTRUMENT = new NullInstrument();
	
	public static final InstrumentGUID NULL_GUID = new NullInstrumentGUID();
	
	public static final MarketCurrency NULL_CURRENCY = MarketCurrency.NULL_CURRENCY;
	
}
