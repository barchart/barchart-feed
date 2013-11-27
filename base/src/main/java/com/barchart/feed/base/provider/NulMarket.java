/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.BookSet;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.SessionSet;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;

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
		return Instrument.NULL;
	}

	@Override
	public Trade trade() {
		return Trade.NULL;
	}

	@Override
	public Book book() {
		return Book.NULL;
	}

	@Override
	public Cuvol cuvol() {
		return Cuvol.NULL;
	}

	@Override
	public Session session() {
		return Session.NULL;
	}
	
	@Override
	public SessionSet sessionSet() {
		return SessionSet.NULL;
	}

	@Override
	public Time updated() {
		return Time.NULL;
	}

	@Override
	public BookSet bookSet() {
		throw new UnsupportedOperationException("Currently not supported");
		//return BookSet.NULL;
	}

	@Override
	public Set<Component> change() {
		return Collections.emptySet();
	}
	
}
