/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.market.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.collections.BitSetEnum;

public enum MarketEvent implements BitSetEnum<MarketEvent> {

	/*
	 * TODO reorder such that more "important" events are ahead of less
	 * "important"; this affects event firing order in case of simultaneous
	 * events
	 */

	//

	/** fires on any change */
	MARKET_UPDATED, //

	MARKET_STATUS_CHANGED, //

	MARKET_OPENED, //
	MARKET_CLOSED, //

	//

	NEW_TRADE_NET, //
	NEW_TRADE_PIT, //
	NEW_TRADE, // combo

	//

	// snapshots
	NEW_BAR_CURRENT_NET, //
	NEW_BAR_CURRENT_PIT, //
	NEW_BAR_CURRENT_EXT, //
	NEW_BAR_CURRENT, // combo

	// NEW_BAR_PREVIOUS_NET, //
	// NEW_BAR_PREVIOUS_PIT, //
	NEW_BAR_PREVIOUS, // combo

	//

	// all combo; from BAR_CURRENT
	NEW_OPEN, //
	NEW_HIGH, //
	NEW_LOW, //
	NEW_CLOSE, // same as LAST
	NEW_SETTLE, //
	NEW_VOLUME, //
	NEW_INTEREST, //

	//

	NEW_BOOK_ERROR, // debug use only
	//
	NEW_BOOK_SNAPSHOT, //
	NEW_BOOK_UPDATE, // includes top
	NEW_BOOK_TOP, // top only; bid or ask or both

	//

	// all combo
	NEW_CUVOL_SNAPSHOT, //
	NEW_CUVOL_UPDATE, //

	;

	private static final Logger log = LoggerFactory
			.getLogger(MarketEvent.class);

	private final long mask = ONE << ordinal();

	@Override
	public final long mask() {
		return mask;
	}

	private static final MarketEvent[] ENUM_VALUES = values();

	private static final int ENUM_SIZE = ENUM_VALUES.length;

	public static final int size() {
		return ENUM_SIZE;
	}

	static {
		log.debug("market event count : {}", ENUM_SIZE);
	}

	/** does this event require book feed ? */
	boolean isBook() {
		switch (this) {
		case NEW_BOOK_SNAPSHOT:
		case NEW_BOOK_UPDATE:
		case NEW_BOOK_TOP:
			return true;
		default:
			return false;
		}
	}

	/** does this event require cumulative volume feed ? */
	boolean isCuvol() {
		switch (this) {
		case NEW_CUVOL_SNAPSHOT:
		case NEW_CUVOL_UPDATE:
			return true;
		default:
			return false;
		}
	}

	/** convenience array wrapper */
	public static MarketEvent[] in(final MarketEvent... events) {
		return events;
	}

}
