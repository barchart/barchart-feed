package com.barchart.feed.api.market;

import com.barchart.feed.api.data.framework.Instrument;

public interface InstrumentFilter {

	boolean filter(Instrument instrument);
	
}
