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
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.common.anno.NotMutable;
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
		return ValueConverter.price(get(MarketBarField.SETTLE_PREVIOUS));
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
		throw new UnsupportedOperationException();
	}

	public Time timeUpdated() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Time timeClosed() {
		return ValueConverter.time(get(MarketBarField.TRADE_DATE));
	}

	@Override
	public boolean isSettled() {
		return get(MarketBarField.IS_SETTLED).asBoolean();
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

}
