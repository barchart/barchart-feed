/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.enums;

import com.barchart.util.values.api.Value;

public enum MarketBookAction implements Value<MarketBookAction> {

	/** no operation */
	NOOP, //

	/** add, new, change, overlay, modify */
	MODIFY, //

	/** clear, delete, remove */
	REMOVE, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketBookAction[] ENUM_VALUES = values();

	public static final MarketBookAction fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	@Override
	public MarketBookAction freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NOOP;
	}

}
