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
import com.barchart.missive.core.Missive;

public abstract class InstrumentBase extends Missive implements Instrument {
	
	static {
		install(InstrumentField.FIELDS);
	}
	
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

}
