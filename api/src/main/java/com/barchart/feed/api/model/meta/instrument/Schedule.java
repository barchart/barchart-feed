package com.barchart.feed.api.model.meta.instrument;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.joda.time.DateTime;

import com.barchart.util.value.api.Existential;

public interface Schedule extends List<TimeSpan>, Existential {

	boolean isOpen(final DateTime instant);

	@Override
	boolean isNull();

	Schedule NULL = new Schedule() {

		@Override
		public int size() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isEmpty() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean contains(final Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<TimeSpan> iterator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T[] toArray(final T[] a) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean add(final TimeSpan e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(final Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(final Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(final Collection<? extends TimeSpan> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(final int index, final Collection<? extends TimeSpan> c) {
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
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TimeSpan get(final int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TimeSpan set(final int index, final TimeSpan element) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(final int index, final TimeSpan element) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TimeSpan remove(final int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int indexOf(final Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int lastIndexOf(final Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ListIterator<TimeSpan> listIterator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ListIterator<TimeSpan> listIterator(final int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<TimeSpan> subList(final int fromIndex, final int toIndex) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public boolean isOpen(final DateTime instant) {
			throw new UnsupportedOperationException();
		}

	};

}
