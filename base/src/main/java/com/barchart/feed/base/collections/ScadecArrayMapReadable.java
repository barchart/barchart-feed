/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import com.barchart.feed.base.values.lang.ScaledDecimal;
import com.barchart.util.common.anno.NotThreadSafe;

// TODO implement LIMIT

@NotThreadSafe
public abstract class ScadecArrayMapReadable<T extends ScaledDecimal<T, ?>, V>
		extends UnsupportedMap<T, V> implements ScadecArrayMap<T, V> {

	public static final int DEFAULT_LIMIT = 1024;

	//

	protected final T keyStep;
	protected final int limit;

	//

	public ScadecArrayMapReadable(final T keyStep) {

		this(keyStep, DEFAULT_LIMIT);

	}

	public ScadecArrayMapReadable(final T keyStep, final int limit) {

		assert keyStep != null;
		assert !keyStep.isZero();

		this.keyStep = keyStep;

		assert limit > 0;

		this.limit = limit;

	}

	@Override
	public final T keyStep() {
		return keyStep;
	}

	@Override
	public final int limit() {
		return limit;
	}

	//

	protected abstract V[] getArray();

	@Override
	public abstract T keyHead();

	@Override
	public abstract T keyTail();

	//

	protected final boolean isNormal(final T key) {
		return key.exponent() == keyStep().exponent();
	}

	protected final boolean isInteger(final long value) {
		return Integer.MIN_VALUE < value && value < Integer.MAX_VALUE;
	}

	protected final boolean isBelowHead(final T key) {
		assert isNormal(key);
		return key.mantissa() < keyHead().mantissa();
	}

	protected final boolean isAboveTail(final T key) {
		assert isNormal(key);
		return key.mantissa() > keyTail().mantissa();
	}

	protected final int keyCount(final T one, final T two) {
		// must be normalized
		assert isNormal(one);
		assert isNormal(two);
		long count = (one.mantissa() - two.mantissa()) / keyStep().mantissa();
		// must fit int
		assert isInteger(count);
		return (int) count;
	}

	protected final int indexFrom(final T key) {
		assert !isBelowHead(key);
		assert !isAboveTail(key);
		final int index = keyCount(key, keyHead());
		assert index >= 0;
		assert index < size();
		return index;
	}

	@Override
	public final int size() {
		return getArray().length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final V get(final Object keyValue) {

		assert keyValue != null;
		assert keyValue instanceof ScaledDecimal<?, ?>;

		final T key = normalize((T) keyValue);

		if (isBelowHead(key) || isAboveTail(key)) {
			return null;
		} else {
			if(getArray() == null || getArray().length > 0) {
				return getArray()[indexFrom(key)];
			} else {
				return null;
			}
		}

	}

	protected final T normalize(final T key) {
		return key.scale(keyStep().exponent());
	}

	@Override
	public final V get(final int index) {
		return getArray()[index];
	}

	@Override
	public final int getIndex(final T keyValue) {

		if (keyValue == null) {
			return ERROR_INDEX;
		}

		final T key = normalize(keyValue);

		if (isBelowHead(key)) {
			return ERROR_INDEX;
		}

		if (isAboveTail(key)) {
			return ERROR_INDEX;
		}

		return indexFrom(key);

	}

}
