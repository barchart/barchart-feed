package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.CalendarField;

public class VarCalendar extends DefCalendar implements MarketDoCalendar {

	@Override
	public <V extends Value<V>> void set(final CalendarField<V> field,
			final V value) {

		assert field != null && value != null;

		// log.debug("field={} value={}", field, value);

		valueArray[field.ordinal()] = value;

	}

	@Override
	public DefCalendar freeze() {

		final DefCalendar calendar = new DefCalendar(valueArray.clone());

		return calendar;

	}

	@Override
	public boolean isFrozen() {

		return false;

	}

}
