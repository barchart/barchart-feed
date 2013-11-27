/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import java.util.List;
import java.util.Map;

/**
 * http://en.wikipedia.org/wiki/Circular_buffer
 */

public interface RingBuffer<V> {

	/** count non null items */
	int count();

	/** length of underlying value array */
	int length();

	/** null array values, set head */
	void clear(int head);

	//

	/** index of first item */
	int head();

	/** index of last item */
	int tail();

	/** index in range : head to tail */
	boolean isValidIndex(int index);

	//

	/** get item at index or throw; no shift */
	V get(int index) throws ArrayIndexOutOfBoundsException;

	/** set item at index between head and tail; no shift */
	void set(int index, V item) throws ArrayIndexOutOfBoundsException;

	/** shift ring to new head; null lost items */
	void setHead(int index, V item);

	/** shift ring to new tail; null lost items */
	void setTail(int index, V item);

	//

	/**
	 * clone of underlying value array; keep null items; with same length;
	 * ordered from head to tail;
	 */
	V[] asArray(Class<V> klaz);

	/**
	 * list only NON null items; ordered from head to tail; list.size() ==
	 * ring.count();
	 */
	List<V> asList();

	/**
	 * map only NON null items; map.size() == ring.count(); key = place;
	 **/
	Map<Integer, V> asMap();

	//

}
