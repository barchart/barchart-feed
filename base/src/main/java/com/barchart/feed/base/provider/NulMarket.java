/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.data.OrderBook;
import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.data.Session;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.data.Trade;
import com.barchart.feed.api.enums.SessionType;
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
	public Market market() {
		return this;
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
	
	@Override
	public Market copy() {
		return this;
	}

}
