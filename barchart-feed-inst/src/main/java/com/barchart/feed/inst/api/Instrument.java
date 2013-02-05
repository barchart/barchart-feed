package com.barchart.feed.inst.api;

import com.barchart.missive.core.MissiveException;
import com.barchart.missive.core.Tag;
import com.barchart.missive.core.TagMap;
import com.barchart.util.values.api.Value;

public interface Instrument extends TagMap, Value<Instrument>,
		Comparable<Instrument> {

	InstrumentGUID getGUID();

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	Instrument NULL_INSTRUMENT = new Instrument() {

		@Override
		public <V> V get(final Tag<V> tag) throws MissiveException {
			return null;
		}

		@Override
		public boolean contains(final Tag<?> tag) {
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
		public int compareTo(final Instrument o) {
			return InstrumentGUID.NULL_INSTRUMENT_GUID.compareTo(o.getGUID());
		}

		@Override
		public InstrumentGUID getGUID() {
			return InstrumentGUID.NULL_INSTRUMENT_GUID;
		}

	};

}
