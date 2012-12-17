/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.enums;

import com.barchart.util.values.api.Value;

public enum MarketTradeSession implements Value<MarketTradeSession> {

	// Null trade session
	NULL_TRADE_SESSION,

	// Default (default session for stocks, pit session for futures)
	DEFAULT,

	// Pit session
	PIT,

	// Combined sessions
	COMBINED,

	// Extended trading (Form-T for stocks, overnight for futures)
	EXTENDED,

	;

	@Override
	public MarketTradeSession freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_TRADE_SESSION;
	}

}