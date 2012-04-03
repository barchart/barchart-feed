package com.ddfplus.feed.common.util.collections;

public interface BitSetLong {

	// return only
	long bitSet();

	// return and update
	long bitSet(long bits);

	// update via and and return
	long bitMaskAnd(long bits);

	// update via xor and return
	long bitMaskXor(long bits);

	// update via or and return
	long bitMaskOr(long bits);

}
