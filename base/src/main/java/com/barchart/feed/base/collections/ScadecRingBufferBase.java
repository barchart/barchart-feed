/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import com.barchart.feed.base.values.lang.ScaledDecimal;
import com.barchart.util.common.math.MathExtra;

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
