package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.instrument.enums.CalendarField;
import com.ddfplus.feed.api.instrument.values.MarketCalendar;

abstract class BaseCalendar extends ValueFreezer<MarketCalendar> implements
		MarketCalendar {

	@Override
	public <V extends Value<V>> V get(final CalendarField<V> field) {
		return field.value();
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(128);

		text.append("Calendar > ");

		for (final CalendarField<?> field : CalendarField.values()) {
			text.append(get(field));
			text.append(" ");
		}

		return text.toString();
	}

	@Override
	public boolean isNull() {
		return this == InstrumentConst.NULL_CALENDAR;
	}

}
