/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.consumer.enums;

import com.barchart.util.values.api.Value;

public enum MarketSide implements Value<MarketSide> {

	/** price gap between bids and asks */
	GAP, //

	/** buy side */
	BID, //

	/** offer side */
	ASK, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketSide[] ENUM_VALUES = values();

	public static final MarketSide fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	@Override
	public MarketSide freeze() {
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
