/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.enums;

import com.barchart.util.values.api.Value;

public enum MarketBookType implements Value<MarketBookType> {

	/** no size book */
	EMPTY, //

	/** only default sizes */
	DEFAULT, //

	/** only implied sizes */
	IMPLIED, //

	/** both default + implied sizes */
	COMBO, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketBookType[] ENUM_VALUES = values();

	public static final MarketBookType fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	@Override
	public MarketBookType freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == EMPTY;
	}

}
