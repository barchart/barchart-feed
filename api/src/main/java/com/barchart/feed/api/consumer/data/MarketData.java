package com.barchart.feed.api.consumer.data;

import com.barchart.util.value.api.Time;

public interface MarketData {

	public Time lastUpdateTime();
	
	public boolean isNull();

}
