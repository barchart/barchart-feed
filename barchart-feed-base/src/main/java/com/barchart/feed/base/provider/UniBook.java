/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.book.api.MarketBook.ENTRY_TOP;
import static com.barchart.feed.base.book.enums.MarketBookSide.GAP;
import static com.barchart.feed.base.book.enums.UniBookResult.DISCARD;
import static com.barchart.feed.base.book.enums.UniBookResult.ERROR;
import static com.barchart.feed.base.book.enums.UniBookResult.NORMAL;
import static com.barchart.feed.base.book.enums.UniBookResult.TOP;
import static com.barchart.feed.base.provider.UniBookRing.CLUE_NONE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

@NotThreadSafe
// javaSize this = 8(obj) + 1(size) + 4(keyRef) + 4(bidsRef) + 4(asksRef) = 24
// javaSize deep = 136 * 2(asks+bids) + 24 (key) = 296
// javaSize = 320
class UniBook<V extends Value<V>> extends ValueFreezer<V> {

	private static Logger log = LoggerFactory.getLogger(UniBook.class);

	protected final byte size;

	protected final PriceValue step;

	protected final UniBookRing bids;
	protected final UniBookRing asks;

	public UniBook(final BookLiquidityType type, final SizeValue size,
			final PriceValue step) throws IllegalArgumentException,
			ArithmeticException {

		this.size = MathExtra.castLongToByte(size.asLong());

		this.step = step;

		this.bids = new UniBookRingBids(this, type);
		this.asks = new UniBookRingAsks(this, type);

		clear();

	}

	public final void clear() {
		// assume central price is zero
		bids.clear(-size);
		asks.clear(0);
	}

	// XXX returns null
	private final UniBookRing ringFor(final MarketBookSide side) {
		switch (side) {
		case BID:
			return bids;
		case ASK:
			return asks;
		default:
			return null;
		}
	}

	private final boolean isTopPlace(final MarketDoBookEntry entry) {
		return entry.place() == ENTRY_TOP;
	}

	private final boolean isValidEntryEnums(final MarketDoBookEntry entry) {

		if (entry == null) {
			return false;
		}

		switch (entry.act()) {
		case MODIFY:
		case REMOVE:
			break;
		default:
			return false;
		}

		switch (entry.side()) {
		case BID:
		case ASK:
			break;
		default:
			return false;
		}

		switch (entry.type()) {
		case DEFAULT:
		case IMPLIED:
			break;
		default:
			return false;
		}

		return true;

	}

	private final boolean isValidPlace(final MarketDoBookEntry entry) {
		final int place = entry.place();
		return ENTRY_TOP <= place && place <= size;
	}

	private final boolean isValidPrice(final MarketDoBookEntry entry) {
		return !entry.price().isNull();
	}

	private final boolean isValidSize(final MarketDoBookEntry entry) {
		return !entry.size().isNull();
	}

	//

	// last updated entry signature
	private byte lastClue;
	private byte lastSide = GAP.ord;

	private final void saveLastClue(final int clue) {
		lastClue = MathExtra.castIntToByte(clue);
	}

	private void saveLastSide(final MarketBookSide side) {
		lastSide = side.ord;
	}

	/**
	 * can return null; returns last changed entry how it looks after
	 * modification;
	 */
	protected final DefBookEntry lastEntry() {
		final MarketBookSide side = MarketBookSide.fromOrd(lastSide);
		final UniBookRing ring = ringFor(side);
		if (ring == null) {
			return null;
		}
		return ring.lastEntry(lastClue);
	}

	public final UniBookResult make(final MarketDoBookEntry entry) {

		make: if (isValidEntryEnums(entry)) {

			final UniBookResult result;

			switch (entry.act()) {
			case MODIFY:
				result = makeModify(entry);
				break;
			case REMOVE:
				result = makeRemove(entry);
				break;
			default:
				break make;
			}

			switch (result) {
			case TOP:
			case NORMAL:
				saveLastSide(entry.side());
				break;
			default:
				break;
			}

			return result;

		}

		return ERROR;

	}

	/**
	 * covers "add", "change", "overlay"
	 **/
	private final UniBookResult makeModify(final MarketDoBookEntry entry) {

		final UniBookRing ring = ringFor(entry.side());

		if (isValidPrice(entry)) {

			final int index = ring.index(entry.price());

			// update top; based on place or price
			if (isTopPlace(entry) || ring.isNewTop(index)) {
				ring.setTop(index, entry);
				saveLastClue(ring.lastClue(index));
				return TOP;
			}

			// update in range
			if (ring.isValidIndex(index)) {
				ring.set(index, entry);
				saveLastClue(ring.lastClue(index));
				return NORMAL;
			}

			// can not update out of range
			return DISCARD;

		}

		// invalid book entry
		return ERROR;

	}

	/**
	 * covers "clear", "delete", "remove"
	 **/
	private final UniBookResult makeRemove(final MarketDoBookEntry entry)
			throws IllegalArgumentException {

		// expecting only NON meaningful size for delete operation
		if (isValidSize(entry)) {
			return ERROR;
		}

		final UniBookRing ring = ringFor(entry.side());

		// try to remove based on price
		if (isValidPrice(entry)) {

			final int index = ring.index(entry.price());

			if (ring.isValidIndex(index)) {
				final boolean isOldTop = ring.isOldTop(index);
				ring.set(index, entry);
				saveLastClue(index);
				if (isOldTop) {
					return TOP;
				} else {
					return NORMAL;
				}
			} else {
				return DISCARD;
			}

		}

		// try to remove based on place
		if (isValidPlace(entry)) {

			final int clue = ring.placeRemove(entry);

			if (clue == CLUE_NONE) {
				// no such place
				return DISCARD;
			} else {
				saveLastClue(clue);
				if (isTopPlace(entry)) {
					return TOP;
				} else {
					return NORMAL;
				}
			}

		}

		// invalid book entry
		return ERROR;

	}

	/**
	 * can return null
	 **/
	protected final MarketDoBookEntry topFor(final MarketBookSide side) {

		final UniBookRing ring = ringFor(side);

		if (ring == null) {
			return null;
		}

		final int index = ring.indexTop();

		if (ring.isValidIndex(index)) {
			return ring.get(index);
		} else {
			return null;
		}

	}

	private final DefBookEntry nullEntry(final int index) {
		return new DefBookEntry(null, GAP, BookLiquidityType.COMBINED, 0, step.mult(index), null);
	}

	@Override
	public String toString() {

		final StringBuilder text = new StringBuilder(1024);

		for (int index = bids.head(); index <= bids.tail(); index++) {
			MarketDoBookEntry entry = bids.get(index);
			if (entry == null) {
				entry = nullEntry(index);
			}
			text.append(entry);
			text.append("\n");
		}

		final int gap = asks.head() - bids.tail();
		if (gap <= size) {
			for (int index = bids.tail() + 1; index < asks.head(); index++) {
				final DefBookEntry entry = nullEntry(index);
				text.append(entry);
				text.append("\n");
			}
		}

		for (int index = asks.head(); index <= asks.tail(); index++) {
			MarketDoBookEntry entry = asks.get(index);
			if (entry == null) {
				entry = nullEntry(index);
			}
			text.append(entry);
			text.append("\n");
		}

		return text.toString();

	}

	@Override
	public V freeze() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

	protected final boolean isEmpty(final MarketBookSide side) {
		final UniBookRing ring = ringFor(side);
		if (ring == null) {
			return true;
		} else {
			return ring.isPlaceEmpty();
		}
	}

	@SuppressWarnings("unused")
	private final int ringGap() {
		return asks.head() - bids.tail();
	}

	// non null entries only, ordered by logical offset
	protected final DefBookEntry[] entriesFor(final MarketBookSide side) {
		final UniBookRing ring = ringFor(side);
		if (ring == null) {
			throw new IllegalArgumentException("invalid book side=" + side);
		} else {
			return ring.entries();
		}
	}

	protected final SizeValue[] sizesFor(final MarketBookSide side) {
		final UniBookRing ring = ringFor(side);
		if (ring == null) {
			throw new IllegalArgumentException("invalid book side=" + side);
		} else {
			return ring.sizes();
		}
	}

}
