/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.enums;

import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.Value;

public enum BookLiquidity implements Value<BookLiquidity> {

	/** no size book */
	NONE, //

	/** only default sizes */
	DEFAULT, //

	/** only implied sizes */
	IMPLIED, //

	/** both default + implied sizes */
	COMBINED, //

	;

	public final byte ord = (byte) ordinal();

	private static final BookLiquidity[] ENUM_VALUES = values();

	public static final BookLiquidity fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	public static final BookLiquidity fromText(final TextValue type) {
		for (final BookLiquidity t : values()) {
			if (type.compareTo(t.name()) == 0) {
				return t;
			}
		}
		return NONE;
	}

	@Override
	public BookLiquidity freeze() {
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
