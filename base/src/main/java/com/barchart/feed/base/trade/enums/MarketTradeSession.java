/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.enums;

import com.barchart.feed.api.model.data.Trade;
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
	
	public Trade.Session asSession() {
		
		switch(this) {
		default:
			return Trade.Session.NULL;
		case DEFAULT:
			return Trade.Session.DEFAULT;
		case PIT:
			return Trade.Session.PIT;
		case COMBINED:
			return Trade.Session.COMBINED;
		case EXTENDED:
			return Trade.Session.EXTENDED;
		}
		
	}

}