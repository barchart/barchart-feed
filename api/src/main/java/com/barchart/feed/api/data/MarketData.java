package com.barchart.feed.api.data;

import com.barchart.util.value.api.Copyable;
import com.barchart.util.value.api.Time;

public interface MarketData<V extends MarketData<V>> extends Copyable<V> {

	public Instrument instrument();
	
	public Time lastUpdateTime();
	
	public boolean isNull();

}
