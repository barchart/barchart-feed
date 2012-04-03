package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.CalendarField;

class DefCalendar extends BaseCalendar {

	protected final static int ARRAY_SIZE = CalendarField.size();

	protected final Value<?>[] valueArray;

	DefCalendar() {
		valueArray = new Value<?>[ARRAY_SIZE];
	}

	DefCalendar(final Value<?>[] valueArray) {

		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;

		this.valueArray = valueArray;

	}

	@Override
	public <V extends Value<V>> V get(final CalendarField<V> field) {

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
