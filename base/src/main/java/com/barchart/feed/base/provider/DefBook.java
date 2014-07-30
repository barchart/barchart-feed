/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.provider.MarketConst.NULL_BOOK_ENTRY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.provider.ValueConst;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.common.anno.NotMutable;
import com.barchart.util.value.api.Time;

@NotMutable
public class DefBook extends ValueFreezer<MarketBook> implements MarketBook {

	private final Instrument instrument;
	
	private final TimeValue time;

	private final MarketBookEntry[] bids;
	private final MarketBookEntry[] asks;
	
	private final MarketBookEntry bidTop;
	private final MarketBookEntry askTop;
	
	private final MarketBookEntry lastUpdate; 
	
	private Set<Component> changeSet;

	public DefBook(final Instrument instrument, final TimeValue time, 
			final MarketBookEntry[] bids, final MarketBookEntry[] asks,
			final MarketBookEntry bidTop, final MarketBookEntry askTop,
			final MarketBookEntry lastUpdate, 
			final Set<Component> changeSet) {

		assert time != null;
		assert bids != null;
		assert asks != null;
		
		this.instrument = instrument;

		this.time = time;

		this.bids = bids;
		this.asks = asks;
		
		this.bidTop = bidTop;
		this.askTop = askTop;
		
		this.lastUpdate = lastUpdate;
		
		this.changeSet = changeSet;

	}

	@Override
	public MarketBookEntry[] entries(final Book.Side side) {
		switch (side) {
		case BID:
			return bids;
		case ASK:
			return asks;
		default:
			throw new IllegalArgumentException("invalid book side=" + side);
		}
	}

	@Override
	public final String toString() {

		final int bidDepth = bids.length;

		final int askDepth = asks.length;

		final int maxDepth = Math.max(bidDepth, askDepth);

		final StringBuilder text = new StringBuilder(512);

		text.append(String.format("%55s", "BID"));
		text.append(String.format("%3s", " | "));
		text.append(String.format("%55s", "ASK"));
		text.append("\n");

		for (int k = 0; k < maxDepth; k++) {

			final int bidsOffset = maxDepth - k - 1;
			final int asksOffset = k;

			final MarketBookEntry bidEntry;
			if (k < bidDepth) {
				bidEntry = bids[bidsOffset];
			} else {
				bidEntry = NULL_BOOK_ENTRY;
			}

			final MarketBookEntry askEntry;
			if (k < askDepth) {
				askEntry = asks[asksOffset];
			} else {
				askEntry = NULL_BOOK_ENTRY;
			}

			text.append(String.format("%55s", bidEntry));
			text.append(String.format("%3s", " | "));
			text.append(String.format("%55s", askEntry));
			text.append("\n");

		}

		return text.toString();

	}

	@Override
	public boolean isNull() {
		return this == MarketConst.NULL_BOOK;
	}

	@Override
	public TimeValue time() {
		return time;
	}

	private static boolean isValid(final Object[] array) {
		return array != null && array.length > 0;
	}

	@Override
	public PriceValue priceTop(final Book.Side side) {
		switch (side) {
		default:
		case BID:
			if (bidTop != null && !bidTop.isNull()) {
				return bidTop.priceValue();
			} else {
				return ValueConst.NULL_PRICE;
			}
		case ASK:
			if (askTop != null && !askTop.isNull()) {
				return askTop.priceValue();
			} else {
				return ValueConst.NULL_PRICE;
			}
		}
	}

	@Override
	public SizeValue sizeTop(final Book.Side side) {
		switch (side) {
		default:
		case BID:
			if (bidTop != null && !bidTop.isNull()) {
				return bidTop.sizeValue();
			} else {
				return ValueConst.NULL_SIZE;
			}
		case ASK:
			if (askTop != null && !askTop.isNull()) {
				return askTop.sizeValue();
			} else {
				return ValueConst.NULL_SIZE;
			}
		}
	}

	@Override
	public SizeValue[] sizes(final Book.Side side) {

		final int limit = MarketBook.ENTRY_LIMIT;

		final SizeValue[] valueArray = new SizeValue[limit];
		for (int place = 0; place < limit; place++) {
			valueArray[place] = ValueConst.NULL_SIZE;
		}

		final MarketBookEntry[] entryArray;
		switch (side) {
		default:
		case BID:
			entryArray = bids;
			break;
		case ASK:
			entryArray = asks;
			break;
		}

		if (isValid(entryArray)) {
			for (final MarketBookEntry entry : entryArray) {
				valueArray[entry.place()] = entry.sizeValue();
			}
		}

		return valueArray;

	}

	@Override
	public Top top() {
		
		final MarketBookEntry bid;
		if(bidTop == null || bidTop.isNull()) {
			bid = NULL_BOOK_ENTRY;
		} else {
			bid = bidTop;
		}
		
		final MarketBookEntry ask;
		if(askTop == null || askTop.isNull()) {
			ask = NULL_BOOK_ENTRY;
		} else {
			ask = askTop;
		}
		
		return new DefBookTop(instrument, time,	bid, ask);
	}
	
	@Override
	public PriceValue priceGap() {

		final PriceValue priceBid = priceTop(Book.Side.BID);
		if (priceBid.isNull()) {
			return ValueConst.NULL_PRICE;
		}

		final PriceValue priceAsk = priceTop(Book.Side.ASK);
		if (priceAsk.isNull()) {
			return ValueConst.NULL_PRICE;
		}

		return priceAsk.sub(priceBid);

	}

	@Override
	public List<Entry> entryList(final Book.Side side) {
		
		final List<Entry> result = new ArrayList<Entry>();
		
		final MarketBookEntry[] e = entries(side);
		
		if(e == null || e.length == 0) {
			return result;
		}
		
		Collections.addAll(result, e);
		
		/* Clean out bad entries */
		final Iterator<Entry> iter = result.iterator();
		while(iter.hasNext()) {
			final Entry en = iter.next();
			if(en == null || en.isNull() || en.size().isNull() || en.price().isNull()) {
				iter.remove();
			}
		}
		
		return Collections.unmodifiableList(result);
		
	}
	
	@Override
	public Entry lastBookUpdate() {
		return lastUpdate;
	}

	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public Time updated() {
		return ValueConverter.time(time);
	}

	@Override
	public Set<Component> change() {
		return changeSet;
	}

}
