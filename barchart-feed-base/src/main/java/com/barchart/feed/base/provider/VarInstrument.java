/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.util.values.api.Value;

public class VarInstrument extends DefInstrument implements MarketDoInstrument {

	@Override
	public <V extends Value<V>> void set(final InstrumentField<V> field,
			final V value) {

		assert field != null && value != null;

		// log.debug("field={} value={}", field, value);

		valueArray[field.ordinal()] = value;

	}

	@Override
	public DefInstrument freeze() {

		// XXX note: since instrument is not nested
		final DefInstrument instrument = new DefInstrument(valueArray.clone());

		return instrument;

	}

	@Override
	public boolean isFrozen() {

		return false;

	}

}
