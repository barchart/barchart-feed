/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.EnumSet;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
public class DefBookTop extends ValueFreezer<MarketBookTop> implements
		MarketBookTop {

	private final Instrument instrument;
	
	private final TimeValue time;

	private final MarketBookEntry bid;
	private final MarketBookEntry ask;

	public DefBookTop(final Instrument instrument, final TimeValue time, 
			final MarketBookEntry bid, final MarketBookEntry ask) {

		assert time != null;

		assert bid != null;
		assert ask != null;
		
		this.instrument = instrument;

		this.time = time;

		this.bid = bid;
		this.ask = ask;

	}

	@Override
	public MarketBookEntry side(final Book.Side side) {
		switch (side) {
		case BID:
			return bid;
		case ASK:
			return ask;
		default:
			throw new IllegalArgumentException("invalid book side");
		}
	}

	@Override
	public String toString() {

		final MarketBookEntry[] bidEntries = new MarketBookEntry[] { bid };

		final MarketBookEntry[] askEntries = new MarketBookEntry[] { ask };

		final DefBook book = new DefBook(instrument, time, bidEntries, askEntries, 
				MarketConst.NULL_BOOK_ENTRY, EnumSet.noneOf(Book.Component.class));

		return book.toString();

	}

	@Override
	public boolean isNull() {
		return this == MarketConst.NULL_BOOK_TOP;
	}

	@Override
	public TimeValue time() {
		return time;
	}

	@Override
	public Book.Entry bid() {
		return bid;
	}

	@Override
	public Book.Entry ask() {
		return ask;
	}

}
