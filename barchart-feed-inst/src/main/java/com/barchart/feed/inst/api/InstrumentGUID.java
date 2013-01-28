package com.barchart.feed.inst.api;

public interface InstrumentGUID extends Comparable<InstrumentGUID> {
	
	public long getGUID();

	@Override
	public boolean equals(Object thatGUID);

	@Override
	public int hashCode();
	
	@Override
	public int compareTo(InstrumentGUID thatGUID);
	
}
