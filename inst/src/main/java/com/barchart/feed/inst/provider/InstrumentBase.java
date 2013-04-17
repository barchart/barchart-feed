/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import com.barchart.feed.api.framework.fields.InstrumentField;
import com.barchart.feed.api.framework.inst.Instrument;
import com.barchart.feed.api.framework.inst.InstrumentGUID;
import com.barchart.missive.core.ObjectMap;

public abstract class InstrumentBase extends ObjectMap implements Instrument {
	
	@Override
	public int compareTo(final Instrument o) {
		return getGUID().compareTo(o.getGUID());
	}
	
	@Override
	public int hashCode() {
		return getGUID().hashCode();
	}
	
	@Override
	public boolean equals(final Object o) {
		if(o instanceof Instrument) {
			return compareTo((Instrument)o) == 0;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isFrozen() {
		return true;
	}
	
	@Override
	public Instrument freeze() {
		return this;
	}

	@Override
	public final boolean isNull() {
		return this == Instrument.NULL_INSTRUMENT;
	}
	
	@Override
	public InstrumentGUID getGUID() {
		return get(InstrumentField.GUID);
	}
	
}
