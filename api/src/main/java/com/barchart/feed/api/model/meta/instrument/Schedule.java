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
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean contains(final Object o) {
			return false;
		}

		@Override
		public Iterator<TimeSpan> iterator() {
			return null;
		}

		@Override
		public Object[] toArray() {
			return null;
		}

		@Override
		public <T> T[] toArray(final T[] a) {
			return null;
		}

		@Override
		public boolean add(final TimeSpan e) {
			return false;
		}

		@Override
		public boolean remove(final Object o) {
			return false;
		}

		@Override
		public boolean containsAll(final Collection<?> c) {
			return false;
		}

		@Override
		public boolean addAll(final Collection<? extends TimeSpan> c) {
			return false;
		}

		@Override
		public boolean addAll(final int index, final Collection<? extends TimeSpan> c) {
			return false;
		}

		@Override
		public boolean removeAll(final Collection<?> c) {
			return false;
		}

		@Override
		public boolean retainAll(final Collection<?> c) {
			return false;
		}

		@Override
		public void clear() {

		}

		@Override
		public TimeSpan get(final int index) {
			return TimeSpan.NULL;
		}

		@Override
		public TimeSpan set(final int index, final TimeSpan element) {
			return TimeSpan.NULL;
		}

		@Override
		public void add(final int index, final TimeSpan element) {

		}

		@Override
		public TimeSpan remove(final int index) {
			return TimeSpan.NULL;
		}

		@Override
		public int indexOf(final Object o) {
			return 0;
		}

		@Override
		public int lastIndexOf(final Object o) {
			return 0;
		}

		@Override
		public ListIterator<TimeSpan> listIterator() {
			return null;
		}

		@Override
		public ListIterator<TimeSpan> listIterator(final int index) {
			return null;
		}

		@Override
		public List<TimeSpan> subList(final int fromIndex, final int toIndex) {
			return null;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public boolean isOpen(final DateTime instant) {
			return false;
		}

	};

}
