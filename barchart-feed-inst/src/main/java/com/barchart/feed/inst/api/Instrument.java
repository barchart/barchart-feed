package com.barchart.feed.inst.api;

import com.barchart.missive.core.TagMap;
import com.barchart.util.values.api.Value;


public interface Instrument extends TagMap, Value<Instrument>, Comparable<Instrument> {
	
	InstrumentGUID getGUID();
	
	@Override
	public boolean equals(Object that);
	
	@Override
	public int hashCode();
	
}
