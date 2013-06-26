/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

public class NulTrade extends ValueFreezer<MarketTrade> implements MarketTrade {

	@Override
	public <V extends Value<V>> V get(final MarketTradeField<V> field) {
		assert field != null;
		return field.value();
	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_TRADE;
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(128);

		text.append("Trade > ");

		for (final MarketTradeField<?> field : MarketTradeField.values()) {
			text.append(get(field));
			text.append(" ");
		}

		// System.err.println(text.length());

		return text.toString();

	}

	@Override
	public MarketTradeType getTradeType() {
		return MarketTradeType.NULL_TRADE_TYPE;
	}

	@Override
	public MarketTradeSession getTradeSession() {
		return MarketTradeSession.NULL_TRADE_SESSION;
	}

	@Override
	public MarketTradeSequencing getTradeSequencing() {
		return MarketTradeSequencing.NULL_TRADE_SEQUENCE;
	}

	@Override
	public Price price() {
		return ValueConst.NULL_PRICE;
	}

	@Override
	public Size size() {
		return ValueConst.NULL_SIZE;
	}

	@Override
	public Time time() {
		return ValueConst.NULL_TIME;
	}

	@Override
	public Time updated() {
		return ValueConst.NULL_TIME;
	}

	@Override
	public Instrument instrument() {
		return Instrument.NULL;
	}

}
