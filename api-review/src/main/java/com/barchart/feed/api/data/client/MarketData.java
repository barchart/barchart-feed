package com.barchart.feed.api.data.client;

import com.barchart.util.value.api.Time;

public interface MarketData {

	public Instrument instrument();
	public Time lastUpdateTime();
	
}
