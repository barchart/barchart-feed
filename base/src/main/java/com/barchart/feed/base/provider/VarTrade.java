/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.trade.api.MarketDoTrade;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.anno.Mutable;

@Mutable
public final class VarTrade extends DefTrade implements MarketDoTrade {

	public VarTrade(final Instrument instrument) {
		super(instrument);
	}
	
	@Override
	public <V extends Value<V>> void set(final MarketTradeField<V> field,
			final V value) {

		assert field != null;
		assert value != null;

		valueArray[field.ordinal()] = value;

	}

	@Override
	public final DefTrade freeze() {

		final DefTrade that = new DefTrade(instrument);

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

		// log.debug("trade={} ", that);

		return that;

	}

	@Override
	public final boolean isFrozen() {
		return false;
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
	
}
