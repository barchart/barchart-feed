package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.framework.InstrumentEntity;
import com.barchart.feed.api.data.value.Time;

public interface MarketData {

	public InstrumentEntity instrument();
	public Time lastUpdateTime();
	
}
