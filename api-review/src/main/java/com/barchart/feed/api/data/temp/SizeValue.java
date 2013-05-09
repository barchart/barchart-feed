package com.barchart.feed.api.data.temp;

public interface SizeValue {

	SizeValue add(SizeValue thatSize);
	SizeValue addLong(long thatSize);
	
	SizeValue mult(SizeValue thatSize);
	SizeValue multLong(long thatSize);
	
}
