/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.inst.enums.MarketBookType;

class UniBookRingAsks extends UniBookRing {

	public UniBookRingAsks(final UniBook<?> book, final MarketBookType type)
			throws IllegalArgumentException {
		super(book, type);
	}

	@Override
	protected final void setTop(final int index, final MarketDoBookEntry entry) {
		setHead(index, entry);
	}

	@Override
	protected final boolean isNewTop(final int indexNew) {
		final int indexTop = indexTop();
		if (isValidIndex(indexTop)) {
			return indexNew < indexTop;
		} else {
			return true;
		}
	}

	@Override
	protected final int indexTop() {
		final int size = length();
		final int placeMask = placeMaskNormal();
		final int offset = Integer.numberOfLeadingZeros(placeMask);
		if (offset >= size) {
			return head() - 1;
		}
		return indexFromOffset(offset);
	}

	@Override
	protected final MarketBookSide side() {
		return MarketBookSide.ASK;
	}

	@Override
	protected final int placeFromClue(final int clue) {
		assert isValidRange(clue) : " clue=" + clue;
		final int mark = mark();
		final int diff = clue - mark;
		final int countMask;
		if (diff >= 0) {
			countMask = ((MASK_HEAD >> (diff)) >>> (mark));
		} else if (diff == -1) {
			countMask = MASK_ONES;
		} else {
			countMask = ~((MASK_HEAD >> (-diff - 2)) >>> (clue + 1));
		}
		return Integer.bitCount(placeMask() & countMask);
	}

	@Override
	protected final int clueFromPlace(/* local */int place) {
		int placeMask = placeMaskNormal();
		int offset = 0;
		while (placeMask != 0) {
			if ((placeMask & MASK_HEAD) != 0) {
				place--;
				if (place == 0) {
					break;
				}
			}
			placeMask <<= 1;
			offset++;
		}
		if (place > 0) {
			return CLUE_NONE;
		} else {
			return clueFromOffset(offset);
		}
	}

}
