/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.consumer.data.Cuvol;
import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.consumer.data.OrderBook;
import com.barchart.feed.api.consumer.data.PriceLevel;
import com.barchart.feed.api.consumer.data.Session;
import com.barchart.feed.api.consumer.data.TopOfBook;
import com.barchart.feed.api.consumer.data.Trade;
import com.barchart.feed.api.consumer.enums.SessionType;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

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

	@Override
	public Instrument instrument() {
		return null;
	}

	@Override
	public Trade lastTrade() {
		return null;
	}

	@Override
	public OrderBook orderBook() {
		return null;
	}

	@Override
	public PriceLevel lastBookUpdate() {
		return null;
	}

	@Override
	public TopOfBook topOfBook() {
		return null;
	}

	@Override
	public Cuvol cuvol() {
		return null;
	}

	@Override
	public Session session(SessionType type) {
		return null;
	}

	@Override
	public Time lastUpdateTime() {
		return null;
	}

}
