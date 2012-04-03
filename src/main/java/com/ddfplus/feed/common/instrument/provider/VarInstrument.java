package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.InstrumentField;

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
