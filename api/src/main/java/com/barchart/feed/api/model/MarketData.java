package com.barchart.feed.api.model;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Copyable;
import com.barchart.util.value.api.Time;

public interface MarketData<V extends MarketData<V>> extends Copyable<V> {

	public Instrument instrument();
	
	public Time updated();
	
	public boolean isNull();

}
