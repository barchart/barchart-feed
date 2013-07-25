/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Set;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

@NotMutable
class DefBar extends NulBar {

	protected final static int ARRAY_SIZE = MarketBarField.size();

	protected final Value<?>[] valueArray;
	protected final Instrument instrument;
	
	private final Set<Component> changeSet;
	
	DefBar(final Instrument instrument, final Set<Component> changeSet) {
		valueArray = new Value<?>[ARRAY_SIZE];
		this.instrument = instrument;
		this.changeSet = changeSet;
	}

	DefBar(final Instrument instrument, final Value<?>[] valueArray,
			final Set<Component> changeSet) {
		assert valueArray != null;
		assert valueArray.length == ARRAY_SIZE;
		this.valueArray = valueArray;
		this.instrument = instrument;
		this.changeSet = changeSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value<V>> V get(final MarketBarField<V> field) {

		assert field != null;

		final V value = (V) valueArray[field.ordinal()];

		if (value == null) {
			return field.value();
		} else {
			return value;
		}

	}
	
	@Override
	public Instrument instrument() {
		return instrument;
	}
	
	@Override
	public Set<Component> change() {
		return changeSet;
	}
	
}
