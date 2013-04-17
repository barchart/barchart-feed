/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.book.enums.MarketBookSide.ASK;
import static com.barchart.feed.base.book.enums.MarketBookSide.BID;
import static com.barchart.feed.base.provider.MarketConst.NULL_BOOK_ENTRY;

import com.barchart.feed.api.commons.enums.BookLiquidityType;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketDoBook;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.ThreadSafe;
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
	public final MarketBookEntry[] entries(final MarketBookSide side) {
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
	public final MarketBookEntry top(final MarketBookSide side) {
		final MarketDoBookEntry entry = topFor(side);
		return entry == null ? NULL_BOOK_ENTRY : entry;
	}

	/* #################################### */

	@Override
	public PriceValue priceGap() {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public final SizeValue[] sizes(final MarketBookSide side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public PriceValue priceTop(final MarketBookSide side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public SizeValue sizeTop(final MarketBookSide side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public UniBookResult setSnapshot(final MarketDoBookEntry[] entries) {
		throw new UnsupportedOperationException("UNUSED");
	}

}
