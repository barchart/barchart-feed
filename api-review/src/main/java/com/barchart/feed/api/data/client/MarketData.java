package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.Time;
import com.barchart.feed.api.data.framework.InstrumentEntity;

public interface MarketData {

	public InstrumentEntity instrument();
	public Time lastUpdateTime();
	
}
