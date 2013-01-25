package com.barchart.feed.inst.api;

import com.barchart.missive.core.TagMap;
import com.barchart.util.values.api.Value;


public interface Instrument extends TagMap, Value<Instrument> {
	
	InstrumentGUID getGUID();
	
}
