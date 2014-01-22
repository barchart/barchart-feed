package com.barchart.feed.base.values.provider;

public class DefBoolean extends BaseBoolean {

	private final Boolean value;

	public DefBoolean(Boolean value) {
		this.value = value;
	}

	@Override
	public Boolean asBoolean() {
		return value;
	}

}
