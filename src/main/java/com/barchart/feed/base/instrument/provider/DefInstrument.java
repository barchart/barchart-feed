/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.provider;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.util.values.api.Value;

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
