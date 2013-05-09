package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.temp.TimeValue;

public interface MarketDataObject {

	public Instrument instrument();
	public TimeValue lastUpdateTime();
	
}
