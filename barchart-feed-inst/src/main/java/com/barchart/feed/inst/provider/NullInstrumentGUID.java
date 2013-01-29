package com.barchart.feed.inst.provider;

import com.barchart.feed.inst.api.InstrumentGUID;

public final class NullInstrumentGUID implements InstrumentGUID {

	@Override
	public long getGUID() {
		return Long.MIN_VALUE;
	}

	@Override
	public int compareTo(final InstrumentGUID thatGUID) {
		if(getGUID() == thatGUID.getGUID()) {
			return 0;
		} else {
			return -1;
		}
	}

}
