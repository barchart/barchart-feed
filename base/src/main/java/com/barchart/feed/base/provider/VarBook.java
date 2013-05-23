/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.api.enums.MarketSide.ASK;
import static com.barchart.feed.api.enums.MarketSide.BID;
import static com.barchart.feed.base.provider.MarketConst.NULL_BOOK_ENTRY;

import java.util.List;

import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketDoBook;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueBuilder;

@Mutable
@ThreadSafe(rule = "use in runSafe() only")
public final class VarBook extends UniBook<MarketBook> implements MarketDoBook {

	private long millisUTC;

	public VarBook(final BookLiquidityType type, final SizeValue size,
			final PriceValue step) {
		super(type, size, step);
	}

	@Override
	public final UniBookResult setEntry(final MarketDoBookEntry entry) {
		return make(entry);
	}

	@Override
	public final MarketBookEntry[] entries(final MarketSide side) {
		return entriesFor(side);
	}

	@Override
	public final DefBook freeze() {
		return new DefBook(time(), entries(BID), entries(ASK), null, null);
	}

	@Override
	public TimeValue time() {
		return ValueBuilder.newTime(millisUTC);
	}

	@Override
	public final void setTime(final TimeValue time) {
		millisUTC = time.asMillisUTC();
	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

	@Override
	public final MarketBookEntry last() {
		final DefBookEntry entry = lastEntry();
		return entry == null ? NULL_BOOK_ENTRY : entry;
	}

	@Override
	public final MarketBookEntry top(final MarketSide side) {
		final MarketDoBookEntry entry = topFor(side);
		return entry == null ? NULL_BOOK_ENTRY : entry;
	}

	/* #################################### */

	@Override
	public PriceValue priceGap() {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public final SizeValue[] sizes(final MarketSide side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public PriceValue priceTop(final MarketSide side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public SizeValue sizeTop(final MarketSide side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public UniBookResult setSnapshot(final MarketDoBookEntry[] entries) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public Price bestPrice(MarketSide side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double bestPriceDouble(MarketSide side) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Size bestSize(MarketSide side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long bestSizeLong(MarketSide side) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PriceLevel> entryList(MarketSide side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price lastPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double lastPriceDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time timeUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time lastUpdateTime() {
		// TODO Auto-generated method stub
		return null;
	}

}
