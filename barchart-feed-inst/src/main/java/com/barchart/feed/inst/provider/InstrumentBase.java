package com.barchart.feed.inst.provider;

import com.barchart.feed.inst.api.Instrument;

public abstract class InstrumentBase implements Instrument {
	
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
