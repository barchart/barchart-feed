package com.barchart.feed.api.market;

import com.barchart.feed.api.data.Instrument;

// Move to sparrow
public interface InstrumentAcceptor {
	
	boolean accept(Instrument instrument);

}
