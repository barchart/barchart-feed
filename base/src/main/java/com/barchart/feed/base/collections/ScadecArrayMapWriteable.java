/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import com.barchart.feed.base.values.lang.ScaledDecimal;
import com.barchart.util.anno.NotThreadSafe;

// TODO implement LIMIT

@NotThreadSafe
public abstract class ScadecArrayMapWriteable<T extends ScaledDecimal<T, ?>, V>
		extends ScadecArrayMapReadable<T, V> {

	//

	public ScadecArrayMapWriteable(final T keyStep) {

		this(keyStep, DEFAULT_LIMIT);

	}

	public ScadecArrayMapWriteable(final T keyStep, final int limit) {

		super(keyStep, limit);

		clear();

	}

	//

	protected abstract void setArray(V[] array);

	protected abstract void keyHead(T price);

	protected abstract void keyTail(T price);

	//

	@SuppressWarnings("unchecked")
	@Override
	public final V put(final T key, final V newValue) {

		assert key != null;
		assert newValue != null;

		// NOTE all prices are normalized to price point
		final T price = normalize(key);

		if (size() == 0) {
			keyHead(price);
			keyTail(price);
			setArray((V[]) new Object[1]);
			getArray()[0] = newValue;
			return null;
		}

		resizeToFit(price);

		final V[] array = getArray();
		final int index = indexFrom(price);
		final V oldValue = array[index];
		array[index] = newValue;

		return oldValue;

	}

	@SuppressWarnings("unchecked")
	protected final void resizeToFit(final T price) {

		int count = 0;

		if (isBelowHead(price)) {
			count = keyCount(price, keyHead());
			assert count < 0;
		}

		if (isAboveTail(price)) {
			count = keyCount(price, keyTail());
			assert count > 0;
		}

		if (count == 0) {
			return;
		}

		final int absCount = Math.abs(count);

		final V[] oldArray = getArray();
		final int oldSize = oldArray.length;

		final int newSize = oldSize + absCount;
		final V[] newArray = (V[]) new Object[newSize];

		final T priceMargin = keyStep().mult(absCount);

		if (count < 0) {
			keyHead(normalize(keyHead().sub(priceMargin)));
			System.arraycopy(oldArray, 0, newArray, absCount, oldSize);
		} else {
			keyTail(normalize(keyTail().add(priceMargin)));
			System.arraycopy(oldArray, 0, newArray, 0, oldSize);
		}

		setArray(newArray);

	}

}
