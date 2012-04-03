package com.ddfplus.feed.common.util.collections;

import com.barchart.util.values.lang.ScaledDecimal;

public class ScadecRingBufferSimple<K extends ScaledDecimal<K, ?>, V> extends
		ScadecRingBufferBase<K, V> {

	@SuppressWarnings("unchecked")
	public ScadecRingBufferSimple(final int size, final K keyStep) {
		checkKeyStep(keyStep);
		this.keyStep = keyStep;
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		this.array = (V[]) new Object[size];
	}

	protected final V[] array;

	protected final K keyStep;

	@Override
	public K keyStep() {
		return keyStep;
	}

	@Override
	public final int length() {
		return array.length;
	}

	@Override
	protected final V arrayGet(final int clue) {
		return array[clue];
	}

	@Override
	protected final void arraySet(final int clue, final V value) {
		array[clue] = value;
	}

	@Override
	protected final boolean isEmpty(final int clue) {
		return array[clue] == null;
	}

}
