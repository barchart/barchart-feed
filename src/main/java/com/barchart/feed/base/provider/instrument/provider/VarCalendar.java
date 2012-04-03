/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.instrument.provider;

import com.barchart.feed.base.api.instrument.enums.CalendarField;
import com.barchart.util.values.api.Value;

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
