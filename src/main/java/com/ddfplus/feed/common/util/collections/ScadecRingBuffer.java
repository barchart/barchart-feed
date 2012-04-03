package com.ddfplus.feed.common.util.collections;

import com.barchart.util.values.lang.ScaledDecimal;

public interface ScadecRingBuffer<K extends ScaledDecimal<K, ?>, V> extends
		RingBuffer<V> {

	int index(K key) throws ArrayIndexOutOfBoundsException;

	//

	K keyStep();

	K keyHead();

	K keyTail();

	//

	V get(K key) throws ArrayIndexOutOfBoundsException;

	void set(K key, V value) throws ArrayIndexOutOfBoundsException;

	//

	void setHead(K key, V value);

	void setTail(K key, V value);

}
