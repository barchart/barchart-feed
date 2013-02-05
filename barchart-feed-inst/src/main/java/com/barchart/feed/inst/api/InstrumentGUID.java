package com.barchart.feed.inst.api;

public interface InstrumentGUID extends Comparable<InstrumentGUID> {

	long getGUID();

	@Override
	boolean equals(Object thatGUID);

	@Override
	int hashCode();

	@Override
	int compareTo(InstrumentGUID thatGUID);

	InstrumentGUID NULL_INSTRUMENT_GUID = new InstrumentGUID() {

		@Override
		public long getGUID() {
			return Long.MIN_VALUE;
		}

		@Override
		public int compareTo(final InstrumentGUID thatGUID) {
			if (getGUID() == thatGUID.getGUID()) {
				return 0;
			} else {
				return -1;
			}
		}

	};

}
