/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.enums;

import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.NORMAL;
import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.UNSEQUENCED;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.COMBINED;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.DEFAULT;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.EXTENDED;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.PIT;

import com.barchart.util.values.api.Value;

public enum MarketTradeType implements Value<MarketTradeType> {

	// Null trade type
	NULL_TRADE_TYPE(DEFAULT, NORMAL),

	// Unknown trade type
	UNKNOWN(DEFAULT, NORMAL),

	/** FUTURES */

	// Composite session (pit + electronic)
	FUTURE_COMPOSITE(COMBINED, NORMAL),
	// Electronic/overnight session
	FUTURE_ELECTRONIC(DEFAULT, NORMAL),
	// Pit / open outcry session
	FUTURE_PIT(PIT, NORMAL),

	/** EQUITIES */

	// Regular trade types
	REGULAR_SALE(DEFAULT, NORMAL),
	ACQUISITION(DEFAULT, NORMAL),
	AMEX_RULE_155(DEFAULT, NORMAL),
	AUTOMATIC_EXECUTION(DEFAULT, NORMAL),
	BUNCHED_TRADE(DEFAULT, NORMAL),
	CROSS_TRADE(DEFAULT, NORMAL),
	DISTRIBUTION(DEFAULT, NORMAL),
	INTERMARKET_SWEEP(DEFAULT, NORMAL),
	MARKET_CLOSING(DEFAULT, NORMAL),
	MARKET_OPENING(DEFAULT, UNSEQUENCED),
	MARKET_REOPENING(DEFAULT, NORMAL),
	NYSE_RULE_127(DEFAULT, NORMAL),
	RESERVED(DEFAULT, NORMAL),
	SPLIT(DEFAULT, NORMAL),
	STOCK_OPTION(DEFAULT, NORMAL),
	STOPPED_STOCK_REGULAR(DEFAULT, NORMAL),
	YELLOW_FLAG(DEFAULT, NORMAL),

	// Regular out-of-sequence trade types
	BUNCHED_SOLD(DEFAULT, UNSEQUENCED),
	DERIVATIVELY_PRICED(DEFAULT, UNSEQUENCED),
	PRIOR_REFERENCE_PRICE(DEFAULT, UNSEQUENCED),
	SOLD_LAST(DEFAULT, UNSEQUENCED),
	SOLD_OOO(DEFAULT, UNSEQUENCED),
	STOPPED_STOCK_OOO(DEFAULT, UNSEQUENCED),
	STOPPED_STOCK_SOLD_LAST(DEFAULT, UNSEQUENCED),

	// Form-T (pre/post-market) trades
	FORM_T(EXTENDED, NORMAL),
	FORM_T_OOO(EXTENDED, UNSEQUENCED),

	;

	public final byte ord;
	public final MarketTradeSession session;
	public final MarketTradeSequencing sequencing;

	private final static MarketTradeType[] ENUM_VALUES = values();
	private final static int ENUM_SIZE = ENUM_VALUES.length;

	public static final int size() {
		return ENUM_SIZE;
	}

	private MarketTradeType(final MarketTradeSession se,
			final MarketTradeSequencing sq) {
		ord = (byte) ordinal();
		session = se;
		sequencing = sq;
	}

	@Override
	public MarketTradeType freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_TRADE_TYPE;
	}

}
