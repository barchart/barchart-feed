/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.provider;

import static com.barchart.feed.base.instrument.enums.InstrumentField.*;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

abstract class BaseInstrument extends ValueFreezer<MarketInstrument> implements
		MarketInstrument {

	@Override
	public <V extends Value<V>> V get(final InstrumentField<V> field) {
		return field.value();
	}

	@Override
	public final int compareTo(final MarketInstrument that) {
		TextValue id1 = this.get(ID);
		TextValue id2 = that.get(ID);
		return id1.compareTo(id2);
	}

	@Override
	public String toString() {
		TextValue id = this.get(ID);
		return String.format("Instrument > %10s", id);// 20
	}

	@Override
	public final boolean equals(final Object thatInst) {
		if (thatInst instanceof MarketInstrument) {
			final MarketInstrument that = (MarketInstrument) thatInst;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public final int hashCode() {
		TextValue id = this.get(ID);
		return id.hashCode();
	}

	@Override
	public boolean isNull() {
		return this == InstrumentConst.NULL_INSTRUMENT;
	}

}
