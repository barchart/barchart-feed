package com.barchart.feed.api.core;

import com.barchart.feed.api.data.Instrument;

public interface InstrumentAcceptor {
	
	boolean accept(Instrument instrument);

}
