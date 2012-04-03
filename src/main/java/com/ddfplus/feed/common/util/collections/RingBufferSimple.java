package com.ddfplus.feed.common.util.collections;

import com.barchart.util.anno.NotThreadSafe;

@NotThreadSafe
public class RingBufferSimple<V> extends RingBufferBase<V> {

	protected final V[] array;

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

	@SuppressWarnings("unchecked")
	public RingBufferSimple(final int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		this.array = (V[]) new Object[size];
	}

	@Override
	protected final boolean isEmpty(final int clue) {
		return array[clue] == null;
	}

}
