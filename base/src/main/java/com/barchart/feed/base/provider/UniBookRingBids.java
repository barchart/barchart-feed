/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.base.book.api.MarketDoBookEntry;

class UniBookRingBids extends UniBookRing {

	public UniBookRingBids(final UniBook<?> book, final BookLiquidityType type)
			throws IllegalArgumentException {
		super(book, type);
	}

	@Override
	protected final void setTop(final int index, final MarketDoBookEntry entry) {
		setTail(index, entry);
	}

	@Override
	protected final boolean isNewTop(final int indexNew) {
		final int indexTop = indexTop();
		if (isValidIndex(indexTop)) {
			return indexNew > indexTop;
		} else {
			return true;
		}
	}

	@Override
	protected final int indexTop() {
		final int size = length();
		final int placeMask = placeMaskNormal() >>> (PLACE_SIZE - size);
		final int offset = Integer.numberOfTrailingZeros(placeMask);
		if (offset >= size) {
			return tail() + 1;
		}
		return indexFromOffset(size + ~offset);
	}

	@Override
	protected final MarketSide side() {
		return MarketSide.BID;
	}

	@Override
	protected int placeFromClue(final int clue) {
		assert isValidRange(clue) : " clue=" + clue;
		final int mark = mark();
		final int diff = clue - mark;
		final int countMask;
		if (diff > 0) {
			countMask = ~((MASK_HEAD >> (diff - 1)) >>> (mark));
		} else if (diff == 0) {
			countMask = MASK_ONES;
		} else {
			countMask = ((MASK_HEAD >> (~diff)) >>> (clue));
		}
		return Integer.bitCount(placeMask() & countMask);
	}

	@Override
	protected final int clueFromPlace(/* local */int place) {
		final int size = length();
		int placeMask = placeMaskNormal() >>> (PLACE_SIZE - size);
		int offset = size - 1;
		while (placeMask != 0) {
			if ((placeMask & MASK_TAIL) != 0) {
				place--;
				if (place == 0) {
					break;
				}
			}
			placeMask >>>= 1;
			offset--;
		}
		if (place > 0) {
			return CLUE_NONE;
		} else {
			return clueFromOffset(offset);
		}
	}

}
