/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.provider;

import com.barchart.feed.base.instrument.enums.CalendarField;
import com.barchart.feed.base.instrument.values.MarketCalendar;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

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
