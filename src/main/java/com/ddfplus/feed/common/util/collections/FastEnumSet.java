package com.ddfplus.feed.common.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.anno.UsedOnce;
import com.ddfplus.feed.common.util.concurrent.Runner;
import com.ddfplus.feed.common.util.concurrent.RunnerLoop;

@NotThreadSafe
public class FastEnumSet<T extends BitSetEnum<T>> extends FastEnumBase<T>
		implements RunnerLoop<T>, Set<T>, BitSetLong {

	protected volatile long bitSet;

	protected FastEnumSet(final T[] values) {
		super(values);
	}

	protected FastEnumSet(final T[] values, final long bitSet) {
		this(values);
		this.bitSet = bitSet;
	}

	protected FastEnumSet(final T[] values, final FastEnumSet<T> enumSet) {
		this(values);
		this.bitSet = enumSet.bitSet;
	}

	protected FastEnumSet(final T[] values, final T... items) {
		this(values);
		for (final T item : items) {
			add(item);
		}
	}

	//

	private static final long NUL = 0L;
	private static final long ONE = 1L;

	@Override
	public void clear() {
		bitSet = NUL;
	}

	@Override
	public boolean add(final T item) {

		final long mask = item.mask();

		long bitSet = this.bitSet;

		final boolean wasEmpty = (bitSet & mask) == NUL;

		bitSet |= mask;

		this.bitSet = bitSet;

		// System.out.println("bitSet and Mas are = " + wasEmpty);
		return wasEmpty;

	}

	@Override
	public boolean remove(final Object item) {

		final long mask = ((BitSetEnum<?>) item).mask();

		long bitSet = this.bitSet;

		final boolean wasFilled = (bitSet & mask) != NUL;

		bitSet &= ~mask;

		this.bitSet = bitSet;

		return wasFilled;

	}

	@UsedOnce
	final class ThisIterator implements Iterator<T> {

		private int index;

		private long bitSet;

		private boolean hasNext;

		private final boolean wasSet() {
			long bitSet = this.bitSet;
			final boolean wasSet = (bitSet & ONE) != NUL;
			bitSet >>>= 1;
			this.bitSet = bitSet;
			return wasSet;
		}

		private ThisIterator(final long bitSet) {
			this.bitSet = bitSet;
		}

		@Override
		public final boolean hasNext() {
			while (!wasSet()) {
				final int i = ++index;
				if (i >= BitSetEnum.LIMIT) {
					hasNext = false;
					return false;
				}
			}
			hasNext = true;
			return true;
		}

		@Override
		public final T next() {
			if (hasNext) {
				hasNext = false;
				final int i = index++;
				return enumValues[i];
			} else {
				throw new IllegalStateException("invalid iterator state");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	@Override
	public Iterator<T> iterator() {
		return new ThisIterator(bitSet);
	}

	@Override
	public final boolean isEmpty() {
		return bitSet == NUL;
	}

	@Override
	public final boolean contains(final Object item) {
		final long mask = ((BitSetEnum<?>) item).mask();
		return (bitSet & mask) != NUL;
	}

	@ThreadSafe(rule = "does not reflect concurrent modifications")
	@Override
	public <R> void runLoop(final Runner<R, T> task, final List<R> list) {

		final T[] enumValues = this.enumValues;

		long bitSet = this.bitSet;

		int index = 0;

		while (bitSet != NUL) {

			if ((bitSet & ONE) != NUL) {

				final T param = enumValues[index];

				final R result = task.run(param);

				if (list == null || result == null) {
					// ignore
				} else {
					list.add(result);
				}

			}

			bitSet >>>= 1;

			index++;

		}
	}

	@Override
	public final int size() {
		return Long.bitCount(bitSet);
	}

	// #####################################################################

	@Override
	public boolean addAll(final Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <X> X[] toArray(final X[] a) {
		throw new UnsupportedOperationException();
	}

	// #####################################################################

	@Override
	public final long bitMaskAnd(final long bits) {
		return (bitSet &= bits);
	}

	@Override
	public final long bitMaskOr(final long bits) {
		return (bitSet |= bits);
	}

	@Override
	public final long bitMaskXor(final long bits) {
		return (bitSet ^= bits);
	}

	@Override
	public final long bitSet() {
		return bitSet;
	}

	@Override
	public final long bitSet(final long bits) {
		final long old = bitSet;
		bitSet = bits;
		return old;
	}

}
