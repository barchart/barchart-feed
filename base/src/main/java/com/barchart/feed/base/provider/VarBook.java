/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.provider.MarketConst.NULL_BOOK_ENTRY;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketDoBook;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueBuilder;

@Mutable
@ThreadSafe(rule = "use in runSafe() only")
public final class VarBook extends UniBook<MarketBook> implements MarketDoBook {

	private long millisUTC;
	
	protected volatile MarketBookEntry lastEntry = MarketConst.NULL_BOOK_ENTRY;
	
	protected Set<Component> changeSet =
			Collections.synchronizedSet(EnumSet.noneOf(Component.class));
	
	public VarBook(final Instrument instrument, final Book.Type type, 
			final SizeValue size, final PriceValue step) {
		super(instrument, type, size, step);
	}

	@Override
	public final UniBookResult setEntry(final MarketDoBookEntry entry) {
		lastEntry = entry.freeze();
		return make(entry);
	}

	@Override
	public final MarketBookEntry[] entries(final Book.Side side) {
		return entriesFor(side);
	}

	@Override
	public final DefBook freeze() {
		return new DefBook(instrument, time(), entries(Book.Side.BID), 
				entries(Book.Side.ASK), lastEntry, EnumSet.copyOf(changeSet));
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
	public final MarketBookEntry top(final Book.Side side) {
		final MarketDoBookEntry entry = topFor(side);
		return entry == null ? NULL_BOOK_ENTRY : entry;
	}

	/* #################################### */

	@Override
	public Instrument instrument() {
		return instrument;
	}
	
	@Override
	public PriceValue priceGap() {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public final SizeValue[] sizes(final Book.Side side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public PriceValue priceTop(final Book.Side side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public SizeValue sizeTop(final Book.Side side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public UniBookResult setSnapshot(final MarketDoBookEntry[] entries) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public List<Entry> entryList(Book.Side side) {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public Top top() {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public Entry lastBookUpdate() {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public Time updated() {
		throw new UnsupportedOperationException("UNUSED");
	}

	@Override
	public Set<Component> change() {
		throw new UnsupportedOperationException("UNUSED");
	}

}
