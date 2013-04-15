/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import org.joda.time.DateTime;

import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

@NotMutable
class DefBar extends NulBar {

	protected final static int ARRAY_SIZE = MarketBarField.size();

	protected final Value<?>[] valueArray;

	DefBar() {
		valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefBar(final Value<?>[] valueArray) {
		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;
		this.valueArray = valueArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketBarField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}
	
	@Override
	public double getOpen() {
		return get(MarketBarField.OPEN).asDouble();
	}

	@Override
	public double getHigh() {
		return get(MarketBarField.HIGH).asDouble();
	}

	@Override
	public double getLow() {
		return get(MarketBarField.LOW).asDouble();
	}

	@Override
	public double getClose() {
		return get(MarketBarField.CLOSE).asDouble();
	}

	@Override
	public double getSettle() {
		return get(MarketBarField.SETTLE).asDouble();
	}

	@Override
	public long getVolume() {
		return get(MarketBarField.VOLUME).asLong();
	}

	@Override
	public long getOpenInterest() {
		return get(MarketBarField.INTEREST).asLong();
	}

	@Override
	public DateTime getLastUpdate() {
		return get(MarketBarField.BAR_TIME).asDateTime();
	}

	@Override
	public DateTime getSessionClose() {
		return get(MarketBarField.TRADE_DATE).asDateTime();
	}

}
