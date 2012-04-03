package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketBarField;

@NotMutable
class DefBar extends NulBar {

	protected final static int ARRAY_SIZE = MarketBarField.size();

	protected final Value<?>[] valueArray;

	DefBar() {
		valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefBar(final Value<?>[] valueArray) {
		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;
		this.valueArray = valueArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketBarField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}

}
