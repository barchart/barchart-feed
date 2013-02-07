/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.values.api.Value;

@Mutable
@ThreadSafe
public final class VarBar extends DefBar implements MarketDoBar {

//	VarBar() {
//		
//	}

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

		return that;

	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

}
