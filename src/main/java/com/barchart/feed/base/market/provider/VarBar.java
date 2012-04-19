/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.market.enums.MarketBarField;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.values.api.Value;

@Mutable
@ThreadSafe
public final class VarBar extends DefBar implements MarketDoBar {

	private static final Logger log = LoggerFactory.getLogger(VarBar.class);

	// public VarBar(PriceValue open, PriceValue high, PriceValue low,
	// PriceValue close, PriceValue settle, SizeValue volume,
	// SizeValue interest, TimeValue time) {
	// this();
	// set(OPEN, open);
	// set(HIGH, high);
	// set(LOW, low);
	// set(CLOSE, close);
	// set(SETTLE, settle);
	// set(VOLUME, volume);
	// set(INTEREST, interest);
	// set(TIME, time);
	// }

	@Override
	public final <V extends Value<V>> void set(final MarketBarField<V> field,
			final V value) {

		// log.debug("field={} value={}", field, value);

		assert field != null;
		assert value != null;

		valueArray[field.ordinal()] = value;

	}

	// remember to freeze values when switching to mutable
	@Override
	public final DefBar freeze() {

		final DefBar that = new DefBar();

		final int size = ARRAY_SIZE;

		final Value<?>[] target = that.valueArray;
		final Value<?>[] source = this.valueArray;

		for (int k = 0; k < size; k++) {

			final Value<?> value = source[k];

			if (value == null) {
				continue;
			}

			target[k] = value.freeze();

		}

		// log.debug("bar={} ", bar);

		return that;

	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

}
