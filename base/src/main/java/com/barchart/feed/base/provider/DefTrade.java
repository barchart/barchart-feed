/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

class DefTrade extends NulTrade {

	protected final static int ARRAY_SIZE = MarketTradeField.size();

	protected final Value<?>[] valueArray;
	
	protected final Instrument instrument;

	DefTrade(final Instrument instrument) {
		
		if(instrument.isNull()) {
			throw new IllegalArgumentException("Instrument cannot be NULL");
		}
		
		this.instrument = instrument;
		this.valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefTrade(final Instrument instrument, final Value<?>[] valueArray) {
		
		if(instrument.isNull()) {
			throw new IllegalArgumentException("Instrument cannot be NULL");
		}

		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;

		this.instrument = instrument;
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
	public Session session() {
		return get(MarketTradeField.SESSION).asSession();
	}
	
	@Override
	public Set<TradeType> types() {
		return Collections.singleton(get(MarketTradeField.TYPE).asType());
	}
	
	@Override
	public Price price() {
		return ValueConverter.price(get(MarketTradeField.PRICE));
	}

	@Override
	public Size size() {
		return ValueConverter.size(get(MarketTradeField.SIZE));
	}

	@Override
	public Time time() {
		return ValueConverter.time(get(MarketTradeField.TRADE_TIME));
	}

	@Override
	public Time updated() {
		// TODO
		return Time.NULL;
	}
	
	@Override
	public Instrument instrument() {
		return instrument;
	}
	
}
