/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import java.util.Map;

import com.barchart.feed.base.values.lang.ScaledDecimal;

public interface ScadecArrayMap<T extends ScaledDecimal<T, ?>, V>
		extends Map<T, V> {

	int limit();

	//

	T keyStep();

	T keyHead();

	T keyTail();

	//

	int ERROR_INDEX = -1;

	int getIndex(T key);

	V get(int index);

	//

}
