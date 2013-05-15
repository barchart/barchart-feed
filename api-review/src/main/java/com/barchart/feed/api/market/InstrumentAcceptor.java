package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.Instrument;

public interface InstrumentAcceptor {
	
	boolean accept(Instrument instrument);

}
