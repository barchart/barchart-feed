package com.barchart.feed.base.trade.enums;

import com.barchart.util.values.api.Value;

public enum MarketTradeSequencing implements Value<MarketTradeSequencing> {

	// Null value
	NULL_TRADE_SEQUENCE,

	// Regular in-sequence trade
	NORMAL,

	// Out-of-sequence trade, may update current high/low but not last
	UNSEQUENCED,

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

}