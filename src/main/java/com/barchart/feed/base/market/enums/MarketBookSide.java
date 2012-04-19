/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.enums;

import com.barchart.util.values.api.Value;

public enum MarketBookSide implements Value<MarketBookSide> {

	/** price gap between bids and asks */
	GAP, //

	/** buy side */
	BID, //

	/** offer side */
	ASK, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketBookSide[] ENUM_VALUES = values();

	public static final MarketBookSide fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	@Override
	public MarketBookSide freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == GAP;
	}

}
