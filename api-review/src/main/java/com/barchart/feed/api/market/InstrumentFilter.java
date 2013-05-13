package com.barchart.feed.api.market;

import com.barchart.feed.api.data.framework.InstrumentEntity;

public interface InstrumentFilter {

	boolean filter(InstrumentEntity instrument);
	
}
