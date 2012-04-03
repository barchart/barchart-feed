package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketTradeField;

class DefTrade extends NulTrade {

	protected final static int ARRAY_SIZE = MarketTradeField.size();

	protected final Value<?>[] valueArray;

	DefTrade() {
		this.valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefTrade(final Value<?>[] valueArray) {

		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;

		this.valueArray = valueArray;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketTradeField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}

}
