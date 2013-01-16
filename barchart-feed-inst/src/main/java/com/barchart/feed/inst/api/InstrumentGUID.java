package com.barchart.feed.inst.api;

public interface InstrumentGUID extends Comparable<InstrumentGUID> {
	
	String getGUID();

	@Override
	boolean equals(Object thatGUID);

	@Override
	int hashCode();
	
	@Override
	int compareTo(InstrumentGUID thatGUID);
	
}
