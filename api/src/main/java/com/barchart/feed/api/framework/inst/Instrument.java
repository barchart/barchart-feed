/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.framework.inst;

import com.barchart.missive.api.Tag;
import com.barchart.missive.api.TagMap;
import com.barchart.missive.core.MissiveException;
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
