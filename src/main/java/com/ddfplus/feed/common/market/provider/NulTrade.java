package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketTradeField;
import com.ddfplus.feed.api.market.values.MarketTrade;

class NulTrade extends ValueFreezer<MarketTrade> implements MarketTrade {

	@Override
	public <V extends Value<V>> V get(final MarketTradeField<V> field) {
		assert field != null;
		return field.value();
	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_TRADE;
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(128);

		text.append("Trade > ");

		for (final MarketTradeField<?> field : MarketTradeField.values()) {
			text.append(get(field));
			text.append(" ");
		}

		// System.err.println(text.length());

		return text.toString();

	}

}
