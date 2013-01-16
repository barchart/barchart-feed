package com.barchart.feed.inst.api;

import com.barchart.feed.inst.enums.InstrumentDefField;
import com.barchart.util.values.api.Value;

public interface InstrumentDef {

	<V extends Value<V>> V get(InstrumentDefField<V> field);
	
}
