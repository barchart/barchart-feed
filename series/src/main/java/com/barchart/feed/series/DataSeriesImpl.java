package com.barchart.feed.series;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.util.value.api.Time;

public class DataSeriesImpl<E extends DataPoint> implements DataSeries<E> {
	/** All {@link DataPoint} are aggregated according to this Period */
	private final Period period;
	/** The backing data */
	private final List<DataPointImpl> data = Collections.synchronizedList(new ArrayList<DataPointImpl>());

	/**
	 * Constructs a new {@link DataSeriesImpl} whose {@link DataPoint} adhere to
	 * the time constraints specified.
	 * 
	 * @param period
	 */
	public DataSeriesImpl(final Period period) {
		this.period = period;
	}

	/**
	 * Returns the {@link Period} which defines the aggregation of time and data
	 * for every {@link DataPointImpl}.
	 *
	 * @return this {@code TimeSeries}' {@link Period}
	 */
	@Override
	public Period getPeriod() {
		return period;
	}

	/**
	 * Returns the earliest date available in this time series.
	 *
	 * @return the earliest date available in this time series.
	 */
	@Override
	public DateTime getStart() {
		if (data.size() == 0)
			return null;

		return data.get(0).getDate();
	}

	/**
	 * Returns the most recent date available in this time series.
	 *
	 * @return the most recent date available in this time series
	 */
	@Override
	public DateTime getEnd() {
		if (data.size() == 0)
			return null;

		return data.get(data.size() - 1).getDate();
	}

	/**
	 * Returns the first {@link DataPointImpl} in this series.
	 *
	 * @return the first {@link DataPointImpl} in this series.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E getFirst() {
		if (data.size() == 0)
			return null;
		return (E) data.get(0);
	}

	/**
	 * Returns the last {@link DataPointImpl} in this series.
	 *
	 * @return the last {@link DataPointImpl} in this series.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E getLast() {
		if (data.size() == 0)
			return null;

		return (E) data.get(data.size() - 1);
	}

	/**
	 * Returns the size of this {@link DataSeries}
	 *
	 * @return the size of this {@link DataSeries}
	 */
	@Override
	public int size() {
		return data.size();
	}

	/**
	 * Returns the {@link DataPointImpl} at the specified index.
	 *
	 * @return the {@link DataPointImpl}
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0
	 *             || index >= size())
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E get(final int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(index + " < 0 || >= " + data.size());
		}
		return (E) data.get(index);
	}

	/**
	 * Returns the {@link DataPointImpl} correlated to the specified time or
	 * null.
	 *
	 * <pre><b>
	 * NOTE: 	All time based comparisons are made at the same {@link PeriodType} resolution as the
	 * 			PeriodType this series is configured with, ignoring other date fields.
	 * </b></pre>
	 *
	 * @param time the time for which the returned {@link DataPointImpl} is
	 *            correlated.
	 * @return the {@link DataPointImpl} correlated to the specified time or
	 *         null
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E forDate(final DateTime date) {
		final int index = indexOf(date, true);
		if (index == -1)
			return null;
		return (E) data.get(index);
	}

	/**
	 * Executes a binary search returning the index of the {@link DataPoint}
	 * containing the date specified, or the index of the previous TimePoint if
	 * the the specified TimePoint is not found.
	 * <p>
	 * This method will always return a valid index. If the specified date is
	 * before the first date within the first TimePoint, 0 will be returned.
	 * Likewise, if the date is after the last date within the last TimePoint in
	 * this series, the index of the last TimePoint will be returned.
	 * <p>
	 *
	 * <pre>
	 * NOTE: 	All time based comparisons are made at the same {@link PeriodType} resolution as the
	 * 			PeriodType this series is configured with, ignoring other date fields --IF--
	 * 			"compareAtRes" is set to true.
	 * </pre>
	 * <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.</b></em>
	 *
	 * @param date the date for which the location index is searched.
	 * @param idxUpper the upper bounds of the search.
	 * @param idxLower the lower bounds of the search.
	 * @param compareAtRes the flag indicating to do comparisons at the
	 *            PeriodType resolution configured
	 * @return the index of the "proper location" whether the date exists in
	 *         this {@code TimeSeries} or not.
	 */
	@Override
	public int closestIndexOf(final Time time, final int idxLower, final int idxUpper, final boolean compareAtRes) {
		if (time == null)
			return -1;
		final DateTime date = new DateTime(time.millisecond());

		final int half = idxLower + (idxUpper - idxLower) / 2;
		if (compareAtRes) {
			if (period.getPeriodType().compareAtResolution(date, data.get(half).date) == 0)
				return half;
		} else {
			if (date.compareTo(data.get(half).date) == 0)
				return half;
		}
		if (half == idxLower) {
			return Math.min(data.size(), half + 1);
		}
		if (date.isAfter(data.get(half).date)) {
			return closestIndexOf(time, idxUpper, half, compareAtRes);
		} else {
			return closestIndexOf(time, half, idxLower, compareAtRes);
		}
	}

	/**
	 * Performs an optimized search returning either the index of the proper
	 * location of the specified date, the index of the matching date found, or
	 * -1.
	 * <p>
	 * If the "exactOnly" flag is true, and the specified date is
	 * <em><b>not</b></em> found, then -1 is returned. If the "exactOnly" flag
	 * is false, and the specified date is <em><b>not</b></em> found, the index
	 * of the location of the date if it were to be inserted into the series, is
	 * returned. Otherwise, the index of the found date is returned.
	 * <p>
	 * Additionally, if the high - low > 128, or the series size > 128, this
	 * method does an interpolated search for the starting mid point of the
	 * binary search, then limits the space of the search to a span of 64
	 * indexes - thus limiting the binary search to a maximum of 6 searches
	 * regardless of the size of this {@code TimeSeries}.
	 * <p>
	 *
	 * <pre>
	 * NOTE: 	All time based comparisons are made at the same {@link PeriodType} resolution as the
	 * 			PeriodType this series is configured with, ignoring other date fields.
	 * </pre>
	 * <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.
	 *
	 * @param time the date for which the location index is searched.
	 * @param exactOnly flag determining whether -1 is returned when the
	 *            specified date is not found. True if -1 is to be returned,
	 *            false if the index of insertion is to be returned.
	 * @return the index of the "proper location" whether the date exists in
	 *         this {@code TimeSeries} or not, when the exactOnly flag is false.
	 *         If true, this method returns -1 if the date is not found.
	 *         Otherwise returns the index of the found date.
	 */
	@Override
	public int indexOf(final DateTime date, final boolean exactOnly) {

		final int size = size();
		int high = size;
		int low = 0;

		if (size > 128) {
			final int interpolatedIndex =
					getInterpolatedIndex(size, date.getMillis(), data.get(0).date.getMillis(),
							data.get(size - 1).date.getMillis(), exactOnly);
			low = Math.max(0, interpolatedIndex - 32);
			high = Math.min(size, interpolatedIndex + 32);
		}

		return indexOf(date, high, low, exactOnly);

	}

	/**
	 * Optimization for binary search to infer the initial boundaries of the
	 * search.
	 *
	 * @param size
	 * @param searchMillis
	 * @param leastMillis
	 * @param mostMillis
	 * @param isExact
	 * @return the interpolated start index which is the "center" of the search
	 *         space.
	 */
	private int getInterpolatedIndex(final int size, final long searchMillis, final long leastMillis, final long mostMillis, final boolean isExact) {
		int mid = (int) (((double) (searchMillis - leastMillis) / (double) (mostMillis - leastMillis)) * size);
		if (mid < 0) {
			mid = isExact ? 0 : -1;
		} else if (mid > size) {
			mid = size;
		}
		return mid;
	}

	/**
	 * Performs an optimized search returning either the index of the proper
	 * location of the specified date, the index of the matching date found, or
	 * -1.
	 * <p>
	 * If the "exactOnly" flag is true, and the specified date is
	 * <em><b>not</b></em> found, then -1 is returned. If the "exactOnly" flag
	 * is false, and the specified date is <em><b>not</b></em> found, the index
	 * of the location of the date if it were to be inserted into the series, is
	 * returned. Otherwise, the index of the found date is returned.
	 * <p>
	 * Additionally, if the high - low > 128, this method does an interpolated
	 * search for the starting mid point of the binary search, then limits the
	 * space of the search to a span of 64 indexes - thus limiting the binary
	 * search to a maximum of 6 searches regardless of the size of this
	 * {@code DataSeries}. <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.
	 *
	 * @param date the date for which the location index is searched.
	 * @param high the upper bounds of the search.
	 * @param low the lower bounds of the search.
	 * @param exactOnly flag determining whether -1 is returned when the
	 *            specified date is not found. True if -1 is to be returned,
	 *            false if the index of insertion is to be returned.
	 *
	 * @return the index of the "proper location" whether the date exists in
	 *         this {@code DataSeries} or not, when the exactOnly flag is false.
	 *         If true, this method returns -1 if the date is not found.
	 *         Otherwise returns the index of the found date.
	 */
	public int indexOf(final DateTime date, int high, int low, final boolean exactOnly) {

		if (high - low > 128)
			return indexOf(date, exactOnly);

		final int size = size();
		int mid = 0;
		while (low <= high) {
			mid = (high + low) / 2;
			if (mid >= size)
				return exactOnly ? -1 : mid;
			final int comparison = date.compareTo(data.get(mid).date);
			if (comparison == 0)
				break;
			if (low == mid && high == mid)
				return exactOnly ? -1 : mid;

			if (comparison < 0) {
				high = mid;
			}
			else if (comparison > 0) {
				low = mid + 1;
			}
		}

		return mid;

	}

	/**
	 * Returns an {@link Iterator} to traverse the contents of this series from
	 * oldest {@link DataPointImpl} to newest DataPoint.
	 *
	 * @return an {@link Iterator} to traverse the contents of this series.
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) data.iterator();
	}

	/**
	 * Returns an {@link Iterator} which traverses this series newest/most
	 * recent first to oldest.
	 *
	 * @return an {@link Iterator} configured to traverse in reverse.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<E> reverseIterator() {
		return new ListIterator<E>() {
			int index = data.size();

			@Override
			public boolean hasNext() {
				return index + 1 < data.size() - 1;
			}

			@Override
			public E next() {
				if (index - 1 < 0)
					throw new NoSuchElementException("" + (index - 1));
				return (E) data.get(index--);
			}

			@Override
			public boolean hasPrevious() {
				return index - 1 > -1;
			}

			@Override
			public E previous() {
				if (index + 1 > data.size() - 1)
					throw new NoSuchElementException("" + (index + 1));
				return (E) data.get(index++);
			}

			@Override
			public int nextIndex() {
				return Math.max(index - 1, 0);
			}

			@Override
			public int previousIndex() {
				return Math.min(index + 1, data.size() - 1);
			}

			@Override
			public void remove() {
				data.remove(index);
			}

			@Override
			public void set(final E e) {
				data.set(index, (DataPointImpl) e);
			}

			@Override
			public void add(final E e) {
				data.add((DataPointImpl) e);
			}
		};
	}

	/**
	 * Returns an array of type E filled with {@link DataPointImpl}s.
	 *
	 * @return an array of type E filled with {@link DataPointImpl}s.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E[] toArray() {
		return (E[]) data.toArray();
	}

	/**
	 * Inserts the {@link DataPointImpl} specified into the backing list in the
	 * appropriate location indicated by the DataPoint's time element.
	 *
	 * @param e the subclass of {@link DataPointImpl} to insert.
	 */
	public void insertData(final E e) {
		data.add(indexOf(e.getDate(), false), (DataPointImpl) e);
	}

	/**
	 * Adds a {@link DataPointImpl} to the end of this {@code TimeSeries}.
	 *
	 * <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.
	 *
	 * @param <E> object which is a subclass of type DataPoint
	 * @return always returns true.
	 * @throws IllegalArgumentException if the DataPoint being added has no time
	 *             value or is otherwise malformed.
	 */
	@Override
	public boolean add(final E e) {
		data.add((DataPointImpl) e);
		return true;
	}

	/**
	 * Adds the specified Object to be the Object residing at the specified
	 * index shifting others to the right.
	 *
	 * <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.
	 *
	 * @param index the index to insert the specified object.
	 * @param e the Object to be set.
	 */
	@Override
	public void add(final int index, final E e) {
		data.add(index, (DataPointImpl) e);
	}

	/**
	 * Sets the specified Object to be the Object residing at the specified
	 * index.
	 *
	 * <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.
	 *
	 * @param index the index to insert the specified object.
	 * @param e the Object to be set.
	 */
	@Override
	public E set(final int index, final E e) {
		data.set(index, (DataPointImpl) e);
		return e;
	}

	/**
	 * Removes the {@link DataPointImpl} at the specified index, and returns it.
	 * Returns null if no DataPoint resides at the index specified. <br>
	 * <br>
	 * <em><b>Warning: this method is not thread-safe.
	 *
	 * @param index the index of the DataPoint to remove.
	 * @return E the DataPoint residing at the specified index or null if no
	 *         DataPoint resides at that index.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E remove(final int index) {
		return (E) data.remove(index);
	}

	/**
	 * Returns a flag indicating whether content exists in this container.
	 *
	 * @return true if this {@code DataSeriesImpl} is empty, false if not.
	 */
	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * Returns a flag indicating whether the specified object is contained in
	 * this {@code DataSeriesImpl} or not.
	 *
	 * @return true if the specified object was found, false if not.
	 */
	@Override
	public boolean contains(final Object o) {
		return data.contains(o);
	}

	/**
	 * Returns a <T> array containing the data within this
	 * {@code DataSeriesImpl}.
	 *
	 * @return <T> array
	 */
	@Override
	public <T> T[] toArray(final T[] a) {
		return data.toArray(a);
	}

	/**
	 * Removes the specified object from this {@code DataSeriesImpl}.
	 *
	 * @return flag indicating whether the specific object was contained in this
	 *         {@code DataSeriesImpl}
	 */
	@Override
	public boolean remove(final Object o) {
		return data.remove(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return data.addAll((Collection<? extends DataPointImpl>) c);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		return data.addAll(index, (Collection<? extends DataPointImpl>) c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(final Collection<?> c) {
		return data.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(final Collection<?> c) {
		return data.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		data.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(final Object o) {
		return data.indexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(final Object o) {
		return data.lastIndexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<E> listIterator() {
		return (ListIterator<E>) data.listIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<E> listIterator(final int index) {
		return (ListIterator<E>) data.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return (List<E>) data.subList(fromIndex, toIndex);
	}
}
