package com.barchart.feed.inst.api;

public interface InstrumentGUID extends Comparable<InstrumentGUID> {
	
	public long getGUID();

	@Override
	public boolean equals(Object thatGUID);

	@Override
	public int hashCode();
	
	@Override
	public int compareTo(InstrumentGUID thatGUID);
	
	public static final InstrumentGUID NULL_INSTRUMENT_GUID = new InstrumentGUID() {

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
		
	};
	
}
