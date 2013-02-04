/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.enums;

import com.barchart.util.values.api.Value;

public enum BookStructure implements Value<BookStructure> {

	/** no size book */
	NONE, //

	/**  */
	PRICE_LEVEL, //

	/**  */
	PRICE_VALUE, //

	/**  */
	ORDER_NUMBER, //

	;

	public final byte ord = (byte) ordinal();

	private static final BookStructure[] ENUM_VALUES = values();

	public static final BookStructure fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	@Override
	public BookStructure freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NONE;
	}

}
