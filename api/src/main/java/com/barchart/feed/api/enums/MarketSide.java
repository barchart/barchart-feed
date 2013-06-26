/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.enums;

enum MarketSide  {

	/** buy side */
	BID, //

	/** offer side */
	ASK, //
	
	NULL

	;

	public final byte ord = (byte) ordinal();

	private static final MarketSide[] ENUM_VALUES = values();

	public static final MarketSide fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

}
