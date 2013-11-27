package com.barchart.feed.base.collections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BlockingConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V>
		implements BlockingMap<K, V> {

	private static final long serialVersionUID = 1L;

	private final ConcurrentMap<K, CountDownLatch> latches =
			new ConcurrentHashMap<K, CountDownLatch>();

	@Override
	public V put(final K key, final V value) {
		final V result = super.put(key, value);
		final CountDownLatch latch = latches.remove(key);
		if (latch != null) {
			latch.countDown();
		}
		return result;
	}

	@Override
	public V putIfAbsent(final K key, final V value) {
		final V result = super.putIfAbsent(key, value);
		final CountDownLatch latch = latches.remove(key);
		if (latch != null) {
			latch.countDown();
		}
		return result;
	}

	@Override
	public V get(final K key, final long timeout, final TimeUnit unit)
			throws InterruptedException {
		V result = get(key);
		if (result == null) {
			CountDownLatch latch = latches.get(key);
			if (latch == null) {
				final CountDownLatch tryLatch = new CountDownLatch(1);
				latch = latches.putIfAbsent(key, tryLatch);
				if (latch == null) {
					latch = tryLatch;
				}
			}
			latch.await(timeout, unit);
			result = get(key);
		}
		return result;
	}

}
