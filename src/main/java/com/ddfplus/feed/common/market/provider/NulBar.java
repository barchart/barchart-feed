package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketBarField;
import com.ddfplus.feed.api.market.values.MarketBar;

@NotMutable
class NulBar extends ValueFreezer<MarketBar> implements MarketBar {

	@Override
	public <V extends Value<V>> V get(final MarketBarField<V> field) {

		assert field != null;

		return field.value();

	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_BAR;
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(384);

		text.append("Bar > ");

		for (final MarketBarField<?> field : MarketBarField.values()) {

			text.append(" ");
			text.append(field.name());
			text.append(" > ");
			text.append(get(field));
			text.append(" ");

		}

		// System.err.println(text.length());

		return text.toString();

	}

}
