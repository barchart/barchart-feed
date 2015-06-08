/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.barchart.feed.api.model.data.parameter.Param;
import com.barchart.feed.api.model.data.parameter.ParamMap;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.feed.base.values.api.BooleanValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.base.values.provider.ValueConst;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.common.anno.NotMutable;
import com.barchart.util.value.api.Bool;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

@NotMutable
public class NulBar extends ValueFreezer<MarketBar> implements MarketBar {
	
	@Override
	public <V extends Value<V>> V get(final MarketBarField<V> field) {

		assert field != null;

		return field.value();

	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_BAR;
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(384);

		text.append("Bar > ");

		for (final MarketBarField<?> field : MarketBarField.values()) {

			text.append(" ");
			text.append(field.name());
			text.append(" > ");
			text.append(get(field));
			text.append(" ");

		}

		return text.toString();

	}

	@Override
	public Price open() {
		return ValueConverter.price(get(MarketBarField.OPEN));
	}

	@Override
	public Price high() {
		return ValueConverter.price(get(MarketBarField.HIGH));
	}

	@Override
	public Price low() {
		return ValueConverter.price(get(MarketBarField.LOW));
	}

	@Override
	public Price close() {
		return ValueConverter.price(get(MarketBarField.CLOSE));
	}

	@Override
	public Price settle() {
		return ValueConverter.price(get(MarketBarField.SETTLE));
	}

	@Override
	public Price previousClose() {
		return ValueConverter.price(get(MarketBarField.CLOSE_PREVIOUS));
	}

	@Override
	public Size volume() {
		return ValueConverter.size(get(MarketBarField.VOLUME));
	}

	@Override
	public Size interest() {
		return ValueConverter.size(get(MarketBarField.INTEREST));
	}

	@Override
	public Time timeOpened() {
		return ValueConverter.time(get(MarketBarField.TRADE_DATE));
	}

	@Override
	public Time timeClosed() {
		return ValueConverter.time(get(MarketBarField.TRADE_DATE));
	}

	@Override
	public Bool isSettled() {
		
		final BooleanValue isSettled = get(MarketBarField.IS_SETTLED);
		
		if(isSettled.isNull()) {
			return ValueConverter.bool(ValueConst.FALSE_BOOLEAN);
		}
		
		return ValueConverter.bool(isSettled);
	}

	@Override
	public Instrument instrument() {
		return Instrument.NULL;
	}

	@Override
	public Time updated() {
		return ValueConverter.time(get(MarketBarField.BAR_TIME));
	}

	@Override
	public Set<Component> change() {
		return Collections.emptySet();
	}

	@Override
	public ParamMap parameters() {
		return parameters;
	}
	
	protected ParamMapImpl parameters = new ParamMapImpl();
	
	protected class ParamMapImpl implements ParamMap {
		
		protected final Map<Param, ValueGetter<?>> params = new EnumMap<Param, ValueGetter<?>>(Param.class);

		@SuppressWarnings("unchecked")
		@Override
		public <V> V get(final Param param) {
			
			if(!params.containsKey(param)) {
				throw new IllegalArgumentException("Parameter " + param + " not in map");
			}
			
			return (V) params.get(param).get();
		}

		@Override
		public boolean has(Param param) {
			return params.containsKey(param);
		}

		@Override
		public synchronized Set<Param> keySet() {
			return EnumSet.<Param> copyOf(params.keySet());
		}

		@Override
		public boolean isNull() {
			return false;
		}
		
	}
	
	protected interface ValueGetter<V> {
		V get();
	}

}
