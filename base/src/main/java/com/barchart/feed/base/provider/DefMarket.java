/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.SessionSet;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;

@NotMutable
public class DefMarket extends NulMarket {
	
	private static final Logger log = LoggerFactory
			.getLogger(DefMarket.class);

	protected final static int ARRAY_SIZE = MarketField.size();

	protected final Value<?>[] valueArray;
	
	protected volatile Time lastUpdateTime = Time.NULL;
	
	protected volatile Instrument instrument;
	
	protected final Set<Component> changeSet = 
		EnumSet.noneOf(Component.class);

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

	@Override
	public Session session() {
		return get(MarketField.BAR_CURRENT);	
	}

	@Override 
	public SessionSet sessionSet() {
		return new FrozenSessionSet(instrument,
				get(MarketField.BAR_CURRENT),
				get(MarketField.BAR_CURRENT_EXT),
				get(MarketField.BAR_PREVIOUS),
				get(MarketField.BAR_PREVIOUS_EXT));
	}
	
	@Override
	public Time updated() {
		return Time.NULL;
	}
	
	@Override
	public Set<Component> change() {
		log.debug("Change set had {} elements", changeSet.size());
		return EnumSet.copyOf(changeSet);
	}
	
}
