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
import static com.barchart.feed.base.trade.enums.MarketTradeSequencing.UNSEQUENCED_VOLUME;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.COMBINED;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.DEFAULT;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.EXTENDED;
import static com.barchart.feed.base.trade.enums.MarketTradeSession.PIT;

import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.base.values.api.Value;

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

	// Volume-only out-of-sequence trade types
	ODD_LOT(DEFAULT, UNSEQUENCED_VOLUME),

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

	public Trade.TradeType asType() {

		switch(this) {

		default:
			return Trade.TradeType.NULL_TRADE_TYPE;
		case UNKNOWN:
			return Trade.TradeType.UNKNOWN;
		case FUTURE_COMPOSITE:
			return Trade.TradeType.FUTURE_COMPOSITE;
		case FUTURE_ELECTRONIC:
			return Trade.TradeType.FUTURE_ELECTRONIC;
		case FUTURE_PIT:
			return Trade.TradeType.FUTURE_PIT;
		case REGULAR_SALE:
			return Trade.TradeType.REGULAR_SALE;
		case ACQUISITION:
			return Trade.TradeType.ACQUISITION;
		case AMEX_RULE_155:
			return Trade.TradeType.AMEX_RULE_155;
		case AUTOMATIC_EXECUTION:
			return Trade.TradeType.AUTOMATIC_EXECUTION;
		case BUNCHED_TRADE:
			return Trade.TradeType.BUNCHED_TRADE;
		case CROSS_TRADE:
			return Trade.TradeType.CROSS_TRADE;
		case DISTRIBUTION:
			return Trade.TradeType.DISTRIBUTION;
		case INTERMARKET_SWEEP:
			return Trade.TradeType.INTERMARKET_SWEEP;
		case MARKET_CLOSING:
			return Trade.TradeType.MARKET_CLOSING;
		case MARKET_OPENING:
			return Trade.TradeType.MARKET_OPENING;
		case MARKET_REOPENING:
			return Trade.TradeType.MARKET_REOPENING;
		case NYSE_RULE_127:
			return Trade.TradeType.NYSE_RULE_127;
		case RESERVED:
			return Trade.TradeType.RESERVED;
		case SPLIT:
			return Trade.TradeType.SPLIT;
		case STOCK_OPTION:
			return Trade.TradeType.STOCK_OPTION;
		case STOPPED_STOCK_REGULAR:
			return Trade.TradeType.STOPPED_STOCK_REGULAR;
		case YELLOW_FLAG:
			return Trade.TradeType.YELLOW_FLAG;
		case BUNCHED_SOLD:
			return Trade.TradeType.BUNCHED_SOLD;
		case DERIVATIVELY_PRICED:
			return Trade.TradeType.DERIVATIVELY_PRICED;
		case PRIOR_REFERENCE_PRICE:
			return Trade.TradeType.PRIOR_REFERENCE_PRICE;
		case SOLD_LAST:
			return Trade.TradeType.SOLD_LAST;
		case SOLD_OOO:
			return Trade.TradeType.SOLD_OOO;
		case STOPPED_STOCK_OOO:
			return Trade.TradeType.STOPPED_STOCK_OOO;
		case STOPPED_STOCK_SOLD_LAST:
			return Trade.TradeType.STOPPED_STOCK_SOLD_LAST;
		case FORM_T:
			return Trade.TradeType.FORM_T;
		case FORM_T_OOO:
			return Trade.TradeType.FORM_T_OOO;
		}

	}

}
