package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.Instrument;

public interface InstrumentFilter {
	
	boolean accept(Instrument instrument);

}
