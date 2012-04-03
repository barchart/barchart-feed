package com.ddfplus.feed.common.util.collections;

import java.util.Map.Entry;

import com.barchart.util.anno.NotThreadSafe;

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