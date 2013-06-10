/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.OrderBook;
import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.data.Session;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.data.Trade;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.values.api.Value;

@NotMutable
public class DefMarket extends NulMarket {

	protected final static int ARRAY_SIZE = MarketField.size();

	protected final Value<?>[] valueArray;
	
	protected volatile Time lastUpdateTime = ValueConst.NULL_TIME;

	public DefMarket() {
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
		return get(MarketField.INSTRUMENT);
	}

	@Override
	public Trade lastTrade() {
		return get(MarketField.TRADE);
	}

	@Override
	public OrderBook orderBook() {
		return get(MarketField.BOOK);
	}

	@Override
	public PriceLevel lastBookUpdate() {
		return get(MarketField.BOOK_LAST);
	}

	@Override
	public TopOfBook topOfBook() {
		return get(MarketField.BOOK_TOP);
	}

	@Override
	public Cuvol cuvol() {
		return get(MarketField.CUVOL);
	}

	@Override
	public Session session() {
		
		return new FrozenSession(get(MarketField.BAR_CURRENT), 
				get(MarketField.BAR_CURRENT_EXT),
				get(MarketField.BAR_PREVIOUS), 
				get(MarketField.BAR_PREVIOUS_EXT));	
		
	}

	@Override
	public Time lastUpdateTime() {
		return null;
	}
	
}
