/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.provider;

import com.barchart.feed.base.market.provider.MarketConst;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

public class NulTrade extends ValueFreezer<MarketTrade> implements MarketTrade {

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
