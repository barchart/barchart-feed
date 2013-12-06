/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.lookup;

import java.util.HashMap;
import java.util.Map;

public final class EnumLookup<K, V extends Enum<?> & Keyable<K>> {

	private final Map<K, V> map;

	private EnumLookup(Map<K, V> map) {
		this.map = map;
	}

	public V get(K key) {
		return map.get(key);
	}

	public static <K, V extends Enum<?> & Keyable<K>> EnumLookup<K, V> create(Class<V> enumClass) {
		V[] enumConstants = enumClass.getEnumConstants();
		Map<K, V> map = new HashMap<K, V>();
		for (V constant : enumConstants) {
			K key = constant.getKey();
			map.put(key, constant);
		}
		return new EnumLookup<K, V>(map);
	}

}
