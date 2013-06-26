/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.enums;

enum BookLiquidityType {

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

	private static final BookLiquidityType[] ENUM_VALUES = values();

	public static final BookLiquidityType fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	public static final BookLiquidityType fromText(final String type) {
		for (final BookLiquidityType t : values()) {
			if (type.compareTo(t.name()) == 0) {
				return t;
			}
		}
		return NONE;
	}

}
