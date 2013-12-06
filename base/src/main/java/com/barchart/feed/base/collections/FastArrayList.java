/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.barchart.feed.base.thread.Runner;
import com.barchart.feed.base.thread.RunnerLoop;
import com.barchart.util.common.anno.NotThreadSafe;
import com.barchart.util.common.anno.ThreadSafe;
import com.barchart.util.common.anno.UsedOnce;

@NotThreadSafe
public class FastArrayList<T> extends UnsupportedList<T> implements
		RunnerLoop<T> {

	protected volatile T[] array;

	@SuppressWarnings("unchecked")
	public FastArrayList() {
		this.array = (T[]) new Object[0];
	}

	@SuppressWarnings("unchecked")
	public FastArrayList(final int size) {
		this.array = (T[]) new Object[size];
	}

	public FastArrayList(final T[] array) {
		this.array = array;
	}

	@Override
	public boolean add(final T item) {
		for (final T known : array) {
			if (known == item) {
				return false;
			}
		}
		final int length = array.length;
		array = Arrays.copyOf(array, length + 1);
		array[length] = item;
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(final Object item) {
		int found = 0;
		for (final T known : array) {
			if (known == item) {
				found++;
			}
		}
		if (found == 0) {
			return false;
		}
		final T[] arrayOld = array;
		final int length = arrayOld.length;
		final T[] arrayNew = (T[]) new Object[length - found];
		int src = 0, tgt = 0;
		for (; src < length;) {
			final T known = arrayOld[src++];
			if (known == item) {
				continue;
			} else {
				arrayNew[tgt++] = known;
			}
		}
		array = arrayNew;
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return new ThisIterator(array);
	}

	//

	@Override
	public int size() {
		return array.length;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@UsedOnce
	final class ThisIterator implements Iterator<T> {

		ThisIterator(final T[] array) {
			this.array = array;
		}

		final T[] array;

		int index;

		boolean hasNext;

		@Override
		public boolean hasNext() {
			if (index < array.length) {
				hasNext = true;
				return true;
			}
			index = 0;
			hasNext = false;
			return false;
		}

		@Override
		public T next() {
			if (hasNext) {
				hasNext = false;
				return array[index++];
			} else {
				throw new IllegalStateException("invalid iterator state");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	@ThreadSafe(rule = "does not reflect concurrent modifications")
	@Override
	public <R> void runLoop(final Runner<R, T> task, final List<R> list) {
		final T[] array = this.array;
		final int length = array.length;
		for (int k = 0; k < length; k++) {
			final R result = task.run(array[k]);
			if (list == null || result == null) {
				continue;
			} else {
				list.add(result);
			}
		}
	}

	/** @return current array */
	@Override
	public Object[] toArray() {
		return array;
	}

	/** @return array copy */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(final T[] instance) {
		return (T[]) Arrays.copyOf(array, size(), instance.getClass());
	}

}
