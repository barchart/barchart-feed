package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketField;

@NotMutable
class DefMarket extends NulMarket {

	protected final static int ARRAY_SIZE = MarketField.size();

	protected final Value<?>[] valueArray;

	DefMarket() {
		valueArray = new Value<?>[ARRAY_SIZE];
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}

}
