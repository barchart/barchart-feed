package com.barchart.feed.base.values.provider;

public class DefBoolean extends BaseBoolean {

	private final boolean value;

	public DefBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public boolean asBoolean() {
		return value;
	}

}
