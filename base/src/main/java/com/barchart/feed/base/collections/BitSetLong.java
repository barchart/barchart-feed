/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

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
