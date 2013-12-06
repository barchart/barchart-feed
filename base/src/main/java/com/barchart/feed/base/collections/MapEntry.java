/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import java.util.Map.Entry;

import com.barchart.util.common.anno.NotThreadSafe;

@NotThreadSafe
class MapEntry<K, V> implements Entry<K, V> {

	K key;
	V value;

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(final V newValue) {
		final V oldValue = value;
		value = newValue;
		return oldValue;
	}

}