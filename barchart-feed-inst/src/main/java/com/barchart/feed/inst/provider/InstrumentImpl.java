/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import com.barchart.feed.api.fields.InstrumentField;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.missive.core.Tag;

class InstrumentImpl extends InstrumentBase implements Instrument {

	static {
		install(new Tag<?>[0]);
	}
	
	@Override
	public Instrument freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == Instrument.NULL_INSTRUMENT;
	}

	@Override
	public InstrumentGUID getGUID() {
		return get(InstrumentField.GUID);
	}
	
}
