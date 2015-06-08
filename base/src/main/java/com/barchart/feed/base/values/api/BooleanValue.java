package com.barchart.feed.base.values.api;

public interface BooleanValue extends Value<BooleanValue>,
		Comparable<BooleanValue> {

	Boolean asBoolean();
	
}
