package com.barchart.feed.inst.provider;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;

public final class NullInstrument implements Instrument {
	
	@Override
	public <V> V get(Tag<V> tag) throws MissiveException {
		return null;
	}

	@Override
	public boolean contains(Tag<?> tag) {
		return false;
	}

	@Override
	public Tag<?>[] tags() {
		return null;
	}

	@Override
	public int size() {
		return 0;
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
		return true;
	}

	@Override
	public InstrumentGUID getGUID() {
		return null;
	}

}
