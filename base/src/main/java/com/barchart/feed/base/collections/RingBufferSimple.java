/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

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
