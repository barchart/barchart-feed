/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

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

		// System.err.println(text.length());

		return text.toString();

	}

	@Override
	public Price open() {
		return ValueConverter.price(get(MarketBarField.OPEN));
	}

	@Override
	public double openDouble() {
		return get(MarketBarField.OPEN).asDouble();
	}

	@Override
	public Price high() {
		return ValueConverter.price(get(MarketBarField.HIGH));
	}

	@Override
	public double highDouble() {
		return get(MarketBarField.HIGH).asDouble();
	}

	@Override
	public Price low() {
		return ValueConverter.price(get(MarketBarField.LOW));
	}
	
	@Override
	public double lowDouble() {
		return get(MarketBarField.LOW).asDouble();
	}

	@Override
	public Price close() {
		return ValueConverter.price(get(MarketBarField.CLOSE));
	}

	@Override
	public double closeDouble() {
		return get(MarketBarField.CLOSE).asDouble();
	}

	@Override
	public Price settle() {
		return ValueConverter.price(get(MarketBarField.SETTLE));
	}

	@Override
	public double settleDouble() {
		return get(MarketBarField.SETTLE).asDouble();
	}
	
	@Override
	public Size volume() {
		return ValueConverter.size(get(MarketBarField.VOLUME));
	}

	@Override
	public long volumeLong() {
		return get(MarketBarField.VOLUME).asLong();
	}

	@Override
	public Size interest() {
		return ValueConverter.size(get(MarketBarField.INTEREST));
	}

	@Override
	public long interestLong() {
		return get(MarketBarField.INTEREST).asLong();
	}

	@Override
	public Time timeOpened() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long timeOpenedLong() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Time timeUpdated() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long timeUpdatedLong() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Time timeClosed() {
		return ValueConverter.time(get(MarketBarField.TRADE_DATE));
	}

	@Override
	public long timeClosedLong() {
		return get(MarketBarField.TRADE_DATE).asMillisUTC();
	}

	@Override
	public Time lastUpdateTime() {
		throw new UnsupportedOperationException();
	}

}
