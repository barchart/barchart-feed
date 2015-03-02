/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.enums;

import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.base.values.api.Value;

public enum MarketTradeSequencing implements Value<MarketTradeSequencing> {

	// Null value
	NULL_TRADE_SEQUENCE,

	// Regular in-sequence trade
	NORMAL,

	// Out-of-sequence trade, may update current high/low but not last
	UNSEQUENCED,

	// Out-of-sequence trade, may only update volume
	UNSEQUENCED_VOLUME,

	;

	@Override
	public MarketTradeSequencing freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_TRADE_SEQUENCE;
	}

	public Trade.Sequence asSequence() {

		switch(this) {
		default:
			return Trade.Sequence.NULL;
		case NORMAL:
			return Trade.Sequence.NORMAL;
		case UNSEQUENCED:
		case UNSEQUENCED_VOLUME:
			return Trade.Sequence.UNSEQUENCED;
		}

	}

}