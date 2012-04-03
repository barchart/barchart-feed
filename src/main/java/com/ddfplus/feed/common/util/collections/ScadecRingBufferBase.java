package com.ddfplus.feed.common.util.collections;

import com.barchart.util.math.MathExtra;
import com.barchart.util.values.lang.ScaledDecimal;

public abstract class ScadecRingBufferBase<K extends ScaledDecimal<K, ?>, V>
		extends RingBufferBase<V> implements ScadecRingBuffer<K, V> {

	@Override
	public abstract K keyStep();

	//

	protected final void checkKeyStep(final K keyStep) {
		if (keyStep == null || keyStep.isZero()) {
			throw new IllegalArgumentException(
					"keyStep == null || keyStep.isZero()");
		}
	}

	@Override
	public final K keyHead() {
		return keyStep().mult(head());
	}

	@Override
	public final K keyTail() {
		return keyStep().mult(tail());
	}

	//

	@Override
	public final int index(final K key) {
		final long count = key.count(keyStep());
		final int index = MathExtra.castLongToInt(count);
		return index;
	}

	@Override
	public final V get(final K key) {
		return get(index(key));
	}

	@Override
	public final void set(final K key, final V value) {
		set(index(key), value);
	}

	@Override
	public final void setHead(final K key, final V value) {
		setHead(index(key), value);
	}

	@Override
	public final void setTail(final K key, final V value) {
		setTail(index(key), value);
	}

}
