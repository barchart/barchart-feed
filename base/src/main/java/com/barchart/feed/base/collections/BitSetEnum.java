/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

// enum represented as long bit set
public interface BitSetEnum<E> {

	long NUL = 0L;

	long ONE = 1L;

	// max enum size
	int LIMIT = 64;

	//

	/** ENUM create order */
	int ordinal();

	//

	/**
	 * implements must provide:
	 * 
	 * private final long mask = ONE << index();
	 * 
	 * public final long mask() { return mask; }
	 * 
	 **/
	long mask();

}
