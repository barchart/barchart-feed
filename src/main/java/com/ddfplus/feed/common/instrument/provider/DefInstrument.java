package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.InstrumentField;

class DefInstrument extends BaseInstrument {

	private final static int ARRAY_SIZE = InstrumentField.size();

	protected final Value<?>[] valueArray;

	DefInstrument() {
		valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefInstrument(final Value<?>[] valueArray) {

		assert valueArray != null;

		this.valueArray = valueArray;

	}

	@Override
	public <V extends Value<V>> V get(final InstrumentField<V> field) {

		assert field != null;

		@SuppressWarnings("unchecked")
		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}

}
