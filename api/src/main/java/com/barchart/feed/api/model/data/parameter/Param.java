package com.barchart.feed.api.model.data.parameter;

import com.barchart.util.value.api.Price;

public enum Param {
	
	SESSION_VWAP(Price.class),
	
	NULL(Void.class)
	
	;
	
	private final Class<?> clazz;
	
	private Param(final Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public <V> Class<V> type() {
		return (Class<V>) clazz;
	}
	
}
