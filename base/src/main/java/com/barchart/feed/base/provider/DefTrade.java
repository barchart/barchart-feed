/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.values.api.Value;

class DefTrade extends NulTrade {

	protected final static int ARRAY_SIZE = MarketTradeField.size();

	protected final Value<?>[] valueArray;

	DefTrade() {
		this.valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefTrade(final Value<?>[] valueArray) {

		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;

		this.valueArray = valueArray;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketTradeField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}

	@Override
	public MarketTradeType getTradeType() {
		return get(MarketTradeField.TYPE);
	}

	@Override
	public MarketTradeSession getTradeSession() {
		return get(MarketTradeField.SESSION);
	}

	@Override
	public MarketTradeSequencing getTradeSequencing() {
		return get(MarketTradeField.SEQUENCING);
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
	
}
