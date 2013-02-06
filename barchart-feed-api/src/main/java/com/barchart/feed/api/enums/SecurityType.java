package com.barchart.feed.api.enums;

import com.barchart.util.values.api.Value;

public enum SecurityType implements Value<SecurityType> {

	NULL_TYPE,
	FOREX,
	INDEX,
	EQUITY,
	FUTURE,
	OPTION,
	SPREAD;

	@Override
	public SecurityType freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_TYPE;
	}
	
}
