/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import org.joda.time.DateTime;

import com.barchart.feed.api.framework.data.InstrumentEntity;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.util.anno.NotMutable;
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
	public InstrumentEntity getInstrument() {
		return InstrumentEntity.NULL_INSTRUMENT;
	}

	@Override
	public DateTime getLastChangeTime() {
		return MarketConst.NULL_DATETIME;
	}

	@Override
	public MarketTrade getLastTrade() {
		return MarketConst.NULL_TRADE;
	}

	@Override
	public MarketBook getBook() {
		return MarketConst.NULL_BOOK;
	}

	@Override
	public MarketBookEntry getLastBookUpdate() {
		return MarketConst.NULL_BOOK_ENTRY;
	}

	@Override
	public MarketBookTop getTopOfBook() {
		return MarketConst.NULL_BOOK_TOP;
	}

	@Override
	public MarketCuvol getCuvol() {
		return MarketConst.NULL_CUVOL;
	}

	@Override
	public MarketCuvolEntry getLastCuvolUpdate() {
		return MarketConst.NULL_CUVOL_ENTRY;
	}

	@Override
	public MarketBar getCurrentSession() {
		return MarketConst.NULL_BAR;
	}

	@Override
	public MarketBar getExtraSession() {
		return MarketConst.NULL_BAR;
	}

	@Override
	public MarketBar getPreviousSession() {
		return MarketConst.NULL_BAR;
	}

}
