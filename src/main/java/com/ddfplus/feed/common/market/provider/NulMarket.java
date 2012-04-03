package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.values.Market;

@NotMutable
class NulMarket extends ValueFreezer<Market> implements Market {

	@Override
	public <V extends Value<V>> V get(final MarketField<V> field) {
		assert field != null;
		return field.value();
	}

	final static String SEPARATOR = "-----------------------------------------------------------------------\n";

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(4 * 1024);

		for (final MarketField<?> field : MarketField.values()) {

			if (field == MarketField.MARKET) {
				continue;
			}

			text.append(SEPARATOR);
			text.append(String.format(" %3d %s \n", field.ordinal(),
					field.name()));
			text.append(get(field));
			text.append("\n");

		}

		text.append(SEPARATOR);

		// System.err.println(text.length());

		return text.toString();

	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_MARKET;
	}

}
