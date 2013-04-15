/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import org.joda.time.DateTime;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

@NotMutable
public class DefMarket extends NulMarket {

	protected final static int ARRAY_SIZE = MarketField.size();

	protected final Value<?>[] valueArray;

	public DefMarket() {
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
	
	@Override
	public Instrument getInstrument() {
		return get(MarketField.INSTRUMENT);
	}
	
	@Override
	public DateTime getLastChangeTime() {
		return get(MarketField.MARKET_TIME).asDateTime();
	}

	@Override
	public MarketTrade getLastTrade() {
		return get(MarketField.TRADE);
	}

	@Override
	public MarketBook getBook() {
		return get(MarketField.BOOK);
	}

	@Override
	public MarketBookEntry getLastBookUpdate() {
		return get(MarketField.BOOK_LAST);
	}

	@Override
	public MarketBookTop getTopOfBook() {
		return get(MarketField.BOOK_TOP);
	}

	@Override
	public MarketCuvol getCuvol() {
		return get(MarketField.CUVOL);
	}

	@Override
	public MarketCuvolEntry getLastCuvolUpdate() {
		return get(MarketField.CUVOL_LAST);
	}

	@Override
	public MarketBar getCurrentSession() {
		return get(MarketField.BAR_CURRENT);
	}

	@Override
	public MarketBar getExtraSession() {
		return get(MarketField.BAR_CURRENT_EXT);
	}

	@Override
	public MarketBar getPreviousSession() {
		return get(MarketField.BAR_PREVIOUS);
	}

}
