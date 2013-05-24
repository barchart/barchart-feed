/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.consumer.enums.MarketSide;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueFreezer;

@NotMutable
public class DefBookTop extends ValueFreezer<MarketBookTop> implements
		MarketBookTop {

	private final TimeValue time;

	private final MarketBookEntry bid;
	private final MarketBookEntry ask;

	public DefBookTop(final TimeValue time, final MarketBookEntry bid,
			final MarketBookEntry ask) {

		assert time != null;

		assert bid != null;
		assert ask != null;

		this.time = time;

		this.bid = bid;
		this.ask = ask;

	}

	@Override
	public MarketBookEntry side(final MarketSide side) {
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

		final DefBook book = new DefBook(time, bidEntries, askEntries,
				MarketConst.NULL_SIZES, MarketConst.NULL_SIZES);

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
	public Time lastUpdateTime() {
		return null; //TODO
	}

}
