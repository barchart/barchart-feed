/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.book.enums.MarketBookAction.*;
import static com.barchart.feed.base.book.enums.MarketBookType.*;
import static com.barchart.util.values.provider.ValueBuilder.*;

import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.book.enums.MarketBookType;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.collections.ScadecRingBufferBase;
import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueBuilder;
import com.barchart.util.values.provider.ValueConst;

/** this book side ring is limited to 32 levels */
@NotThreadSafe
// javaSize this = 8(obj) + 4(defRef) + 4(impRef) + 4(bookRef) + 4(mask) = 24
// javaSize deep = 16(def) + 16(imp) + 10(size) * 2(def+imp) * 4(int) = 112
// javaSize = 136
abstract class UniBookRing extends
		ScadecRingBufferBase<PriceValue, MarketDoBookEntry> {

	//

	// book side-dependent operations

	protected abstract MarketBookSide side();

	protected abstract int indexTop();

	protected abstract void setTop(int index, MarketDoBookEntry entry);

	protected abstract boolean isNewTop(int index);

	protected final boolean isOldTop(final int indexOld) {
		final int indexTop = indexTop();
		if (isValidIndex(indexTop)) {
			return indexOld == indexTop;
		} else {
			return false;
		}
	}

	//

	// use for entry reconstruction
	protected final static MarketBookAction RET_ACT = NOOP;
	protected final static MarketBookType RET_TYPE = COMBO;

	//

	// can be null if not used
	private final int[] arrayDefault;
	private final int[] arrayImplied;

	protected final MarketBookType type() {
		final int maskDefault = arrayDefault == null ? 0x0 : 0x1;
		final int maskImplied = arrayImplied == null ? 0x0 : 0x2;
		switch (maskDefault + maskImplied) {
		default:
		case 0x0:
			return EMPTY;
		case 0x1:
			return DEFAULT;
		case 0x2:
			return IMPLIED;
		case 0x3:
			return COMBO;
		}
	}

	protected final UniBook<?> book;

	public UniBookRing(final UniBook<?> book, final MarketBookType type)
			throws IllegalArgumentException {

		if (book == null) {
			throw new IllegalArgumentException("book == null");
		}

		this.book = book;

		checkKeyStep(book.step);

		final int size = book.size;

		if (size <= 0 || PLACE_SIZE < size) {
			throw new IllegalArgumentException("wrong book size=" + size);
		}

		if (type == null) {
			throw new IllegalArgumentException("type == null");
		}

		switch (type) {
		case EMPTY:
			arrayDefault = null;
			arrayImplied = null;
			break;
		case DEFAULT:
			arrayDefault = new int[size];
			arrayImplied = null;
			break;
		case IMPLIED:
			arrayDefault = null;
			arrayImplied = new int[size];
			break;
		case COMBO:
			arrayDefault = new int[size];
			arrayImplied = new int[size];
			break;
		default:
			throw new IllegalArgumentException("wrong book type=" + type);
		}

	}

	private final int[] arrayFor(final MarketBookType type) {
		switch (type) {
		case DEFAULT:
			return arrayDefault;
		case IMPLIED:
			return arrayImplied;
		default:
			return null;
		}
	}

	private final static int safeGet(final int[] array, final int clue) {
		if (array == null) {
			return 0;
		} else {
			return array[clue];
		}
	}

	private final static void safeSet(final int[] array, final int clue,
			final int value) {
		if (array == null) {
			return;
		} else {
			array[clue] = value;
		}
	}

	// reconstruct entry from components
	// zero size makes null entry
	@Override
	protected final DefBookEntry arrayGet(final int clue) {
		final int sizeCombo = sizeCombo(clue);
		final DefBookEntry entry;
		if (sizeCombo == 0) {
			entry = null;
		} else {
			final int place = placeFromClue(clue);
			final PriceValue price = keyStep().mult(indexFromClue(clue));
			final SizeValue size = newSize(sizeCombo);
			entry = new DefBookEntry(RET_ACT, side(), RET_TYPE, place, price,
					size);
		}
		return entry;
	}

	// disassemble entry into components
	// null entry makes zero size
	@Override
	protected final void arraySet(final int clue, final MarketDoBookEntry entry)
			throws ArithmeticException {
		if (entry == null) {
			safeSet(arrayDefault, clue, 0);
			safeSet(arrayImplied, clue, 0);
		} else {
			final SizeValue entrySize = entry.size();
			final int value;
			if (entrySize == null) {
				value = 0;
			} else {
				// assume can fit
				value = MathExtra.castLongToInt(entrySize.asLong());
			}
			safeSet(arrayFor(entry.type()), clue, value);
		}
		placeUpdate(clue);
	}

	@Override
	public final int length() {
		return book.size;
	}

	private final int sizeCombo(final int clue) throws ArithmeticException {
		final int sizeDefault = safeGet(arrayDefault, clue);
		final int sizeImplied = safeGet(arrayImplied, clue);
		// assume can fit
		final int sizeCombo = MathExtra.intAdd(sizeDefault, sizeImplied);
		return sizeCombo;
	}

	@Override
	public final boolean isEmpty(final int clue) {
		return sizeCombo(clue) == 0;
	}

	@Override
	public final PriceValue keyStep() {
		return book.step;
	}

	//

	protected final String toString(int bits) {
		final int displayMask = 1 << 31;
		final StringBuilder buf = new StringBuilder(35);
		for (int c = 1; c <= 32; c++) {
			buf.append((bits & displayMask) == 0 ? '0' : '1');
			bits <<= 1;
			if (c % 8 == 0) {
				buf.append('-');
			}
		}
		return buf.toString();
	}

	/** non empty only; ordered by offset */
	protected final DefBookEntry[] entries() {

		final DefBookEntry[] entries = new DefBookEntry[placeCount()];

		int entryIndex = 0;

		final int size = length();

		for (int offset = 0; offset < size; offset++) {
			final int clue = clueFromOffset(offset);
			if (isEmpty(clue)) {
				continue;
			} else {
				entries[entryIndex++] = arrayGet(clue);
			}
		}

		assert entryIndex == entries.length;

		return entries;

	}

	//
	protected final int lastClue(final int index) {
		return clueFromIndex(index);
	}

	// reconstruct last entry
	protected final DefBookEntry lastEntry(final int clue) {
		final DefBookEntry entry = arrayGet(clue);
		if (entry == null) {
			final int index = indexFromClue(clue);
			final PriceValue price = keyStep().mult(index);
			return new DefBookEntry(RET_ACT, side(), RET_TYPE, 0, price, null);
		} else {
			return entry;
		}
	}

	/*
	 * ############### entry place functions #################
	 */

	/** invalid clue value */
	protected static final int CLUE_NONE = -1;

	/** number of maximum places in the place mask */
	protected static final int PLACE_SIZE = Integer.SIZE;

	/** only left most (sign) bit set */
	protected static final int MASK_HEAD = 0x80000000;

	/** only right most bit set */
	protected static final int MASK_TAIL = 0x00000001;

	/** all bits set */
	protected static final int MASK_ONES = 0xFFFFFFFF;

	/** no bits set */
	protected static final int MASK_NONE = 0x00000000;

	/**
	 * locations of non null places referenced by clue; clue==0 maps to left
	 * most bit; clue==size-1 maps to right bit up to PLACE_SIZE;
	 */
	private int placeMask;

	protected final int placeMask() {
		return placeMask;
	}

	/** update place mask (free/busy) for a clue */
	private final void placeUpdate(final int clue) {
		assert isValidRange(clue) : " clue=" + clue;
		final int mask = MASK_HEAD >>> clue;
		if (isEmpty(clue)) {
			placeMask &= ~mask;
		} else {
			placeMask |= mask;
		}
	}

	/** unwrap ring place mask from mark to zero offset */
	protected final int placeMaskNormal() {
		final int size = length();
		final int mark = mark();
		final int mask = placeMask;
		return ((mask << mark) | (mask >>> (size - mark)))
				& (MASK_HEAD >> (size - 1));
	}

	/**
	 * number of non null places before and including this entry - as referenced
	 * by absolute clue;
	 */
	protected abstract int placeFromClue(final int clue);

	/**
	 * try to find a clue for a place; returns CLUE_NONE if not found;
	 */
	protected abstract int clueFromPlace(final int place);

	protected final int placeFromOffset(final int offset) {
		return placeFromClue(clueFromOffset(offset));
	}

	protected final int placeRemove(final MarketDoBookEntry entry) {
		final int clue = clueFromPlace(entry.place());
		if (clue == CLUE_NONE) {
			// return false;
		} else {
			safeSet(arrayFor(entry.type()), clue, 0);
			placeUpdate(clue);
			// return true;
		}
		return clue;
	}

	/** use this fast count instead of ring.count() */
	protected final int placeCount() {
		return Integer.bitCount(placeMask);
	}

	protected final boolean isPlaceEmpty() {
		return placeMask == 0;
	}

	protected final SizeValue[] sizes() {

		final int size = length();

		final SizeValue[] sizeArray = new SizeValue[size];

		for (int offset = 0; offset < size; offset++) {

			final int clue = clueFromOffset(offset);

			final SizeValue value;

			if (isEmpty(clue)) {
				value = ValueConst.NULL_SIZE;
			} else {
				value = ValueBuilder.newSize(sizeCombo(clue));
			}

			sizeArray[offset] = value;

		}

		return sizeArray;

	}

}
