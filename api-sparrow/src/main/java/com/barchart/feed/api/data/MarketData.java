package com.barchart.feed.api.data;

import com.barchart.util.value.api.Time;

public interface MarketData {

	public Time lastUpdateTime();
	
	public boolean isNull();

}
