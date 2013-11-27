package com.barchart.feed.base.collections;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface BlockingMap<K, V> extends Map<K, V> {

	public V get(K key, long timeout, TimeUnit unit)
			throws InterruptedException;

}
