package com.barchart.feed.inst.api;

import com.barchart.util.values.api.Value;

public interface InstrumentGUID extends Comparable<InstrumentGUID>, Value<InstrumentGUID>, CharSequence {

	@Override
	boolean equals(Object thatGUID);

	@Override
	int hashCode();

	@Override
	int compareTo(InstrumentGUID thatGUID);
	
	/** null instrument */
	InstrumentGUID NULL_INSTRUMENT_GUID = new InstrumentGUID() {

		@Override
		public int compareTo(final InstrumentGUID thatGUID) {
			if (this == thatGUID) {
				return 0;
			} else {
				return -1;
			}
		}

		@Override
		public InstrumentGUID freeze() {
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
		public int length() {
			return 0;
		}

		@Override
		public char charAt(int index) {
			return 0;
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return null;
		}

	};

}
