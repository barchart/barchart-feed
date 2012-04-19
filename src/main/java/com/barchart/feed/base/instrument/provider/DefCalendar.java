/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.provider;

import com.barchart.feed.base.instrument.enums.CalendarField;
import com.barchart.util.values.api.Value;

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
