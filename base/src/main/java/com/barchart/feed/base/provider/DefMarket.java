/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.values.api.Value;

@NotMutable
public class DefMarket extends NulMarket {

	protected final static int ARRAY_SIZE = MarketField.size();

	protected final Value<?>[] valueArray;
	
	protected volatile Time lastUpdateTime = ValueConst.NULL_TIME;
	
	protected volatile Instrument instrument;

	public DefMarket(final Instrument instrument) {
		
		this.instrument = instrument;
		valueArray = new Value<?>[ARRAY_SIZE];
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}
	
	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public Trade trade() {
		return get(MarketField.TRADE);
	}

	@Override
	public Book book() {
		return get(MarketField.BOOK);
	}

	@Override
	public Cuvol cuvol() {
		return get(MarketField.CUVOL);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Session session() {
		
		return new FrozenSession(
				instrument, 
				get(MarketField.STATE).contains(MarketStateEntry.IS_SETTLED),
				get(MarketField.BAR_CURRENT), 
				get(MarketField.BAR_CURRENT_EXT),
				get(MarketField.BAR_PREVIOUS), 
				get(MarketField.BAR_PREVIOUS_EXT));	
		
	}

	@Override
	public Time updated() {
		return null;
	}
	
}
