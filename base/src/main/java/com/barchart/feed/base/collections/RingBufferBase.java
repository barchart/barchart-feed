/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.barchart.util.common.anno.NotThreadSafe;

/**
 * http://en.wikipedia.org/wiki/Circular_buffer
 * 
 * legend:
 * 
 * absolute :: from beginning of array
 * 
 * clue :: absolute offset of a current item
 * 
 * mark :: absolute offset of a head (first) item
 * 
 * diff :: relative to mark offset of current item
 * 
 * head :: external index of a first logical item
 * 
 * tail :: external index of the last logical item
 * 
 * index :: external absolute index of current item
 * 
 * offset :: relative to head index of current item
 * 
 * place :: logical numbering of non-empty items, starts from 1
 * 
 * rules:
 * 
 * diff == clue - mark;
 * 
 * offset = index - head;
 * 
 * clue == (offset + mark) % length;
 * 
 * index == head + (diff >= 0 ? diff : diff + length );
 * 
 **/
@NotThreadSafe
public abstract class RingBufferBase<V> implements RingBuffer<V> {

	//

	protected abstract V arrayGet(int clue);

	protected abstract void arraySet(int clue, V value);

	protected abstract boolean isEmpty(int clue);

	@Override
	public abstract int length();

	//

	/** internal index of the first item */
	private int mark;

	/** external index of the first item */
	private int head;

	//

	protected final int mark() {
		return mark;
	}

	@Override
	public final int head() {
		return head;
	}

	@Override
	public final int tail() {
		return head + length() - 1;
	}

	protected final boolean isValidRange(final int value) {
		return 0 <= value && value < length();
	}

	/** logical offset of clue from the mark */
	protected final int offsetFromClue(final int clue) {
		assert isValidRange(clue) : " clue=" + clue;
		final int diff = clue - mark;
		return diff >= 0 ? diff : diff + length();
	}

	/** external index for a clue */
	protected final int indexFromClue(final int clue) {
		return head + offsetFromClue(clue);
	}

	/** external index for a logical offset */
	protected final int indexFromOffset(final int offset) {
		assert isValidRange(offset) : " offset=" + offset;
		return head + offset;
	}

	/** absolute clue from logical offset off mark */
	protected final int clueFromOffset(final int offset) {
		final int size = length();
		final int mark = this.mark;
		assert isValidRange(mark) : " mark=" + mark;
		assert isValidRange(offset) : " offset=" + offset;
		int clue = mark + offset;
		if (clue >= size) {
			clue -= size;
		}
		assert isValidRange(clue) : " clue=" + clue;
		return clue;
	}

	/** absolute clue from absolute index */
	protected final int clueFromIndex(final int index) {
		return clueFromOffset(index - head());
	}

	@Override
	public final boolean isValidIndex(final int index) {
		final int head = this.head;
		final int tail = head + length() - 1;
		return head <= index && index <= tail;
	}

	//

	@Override
	public final V get(final int index) {

		final int offset = index - head;
		final int size = length();

		if (offset < 0 || size <= offset) {
			throw new ArrayIndexOutOfBoundsException(" index=" + index
					+ " head=" + head() + " tail=" + tail());
		}

		int clue = mark + offset;
		if (clue >= size) {
			clue -= size;
		}

		assert isValidRange(clue) : " clue=" + clue;

		return arrayGet(clue);

	}

	@Override
	public final void set(final int index, final V value) {

		final int offset = index - head;
		final int size = length();

		if (offset < 0 || size <= offset) {
			throw new ArrayIndexOutOfBoundsException(" index=" + index
					+ " head=" + head() + " tail=" + tail());
		}

		int clue = mark + offset;
		if (clue >= size) {
			clue -= size;
		}

		assert isValidRange(clue) : " clue=" + clue;

		arraySet(clue, value);

	}

	@Override
	public final void setHead(final int index, final V value) {
		if (index == head()) {
			set(index, value);
		} else {
			setMark(0, index, value);
		}
	}

	@Override
	public final void setTail(final int index, final V value) {
		if (index == tail()) {
			set(index, value);
		} else {
			setMark(length() - 1, index, value);
		}
	}

	protected final void setMark(final int zero, final int index, final V value) {

		assert isValidRange(zero) : " zero=" + zero;

		final int size = length();

		int head = this.head;

		int offset = index - (head + zero);

		if (size <= Math.abs(offset)) {
			clear(index - zero);
			arraySet(zero, value);
			return;
		}

		head += offset;

		this.head = head;

		int mark = this.mark;

		assert isValidRange(mark) : " mark=" + mark;

		while (offset < 0) {
			mark--;
			if (mark < 0) {
				mark = size - 1;
			}
			arraySet(mark, null);
			offset++;
		}

		assert isValidRange(mark) : " mark=" + mark;

		while (offset > 0) {
			arraySet(mark, null);
			mark++;
			if (mark == size) {
				mark = 0;
			}
			offset--;
		}

		assert isValidRange(mark) : " mark=" + mark;

		int clue = mark + zero;
		if (clue >= size) {
			clue -= size;
		}

		assert isValidRange(clue) : " clue=" + clue;

		arraySet(clue, value);

		this.mark = mark;

	}

	@Override
	public final void clear(final int head) {

		final int size = length();

		for (int clue = 0; clue < size; clue++) {
			arraySet(clue, null);
		}

		this.mark = 0;
		this.head = head;

	}

	@Override
	public final int count() {

		final int size = length();

		int count = 0;

		for (int clue = 0; clue < size; clue++) {

			if (isEmpty(clue)) {
				continue;
			}

			count++;

		}

		return count;

	}

	@SuppressWarnings("unchecked")
	@Override
	public final V[] asArray(final Class<V> klaz) {

		final int size = length();
		final int mark = mark();

		final V[] clone = (V[]) Array.newInstance(klaz, size);

		for (int spot = 0; spot < size; spot++) {

			int clue = mark + spot;
			if (clue >= size) {
				clue -= size;
			}

			assert isValidRange(clue) : " clue=" + clue;

			if (isEmpty(clue)) {
				continue;
			}

			clone[spot] = arrayGet(clue);

		}

		return clone;

	}

	@Override
	public final List<V> asList() {

		final int size = length();
		final int mark = mark();

		final List<V> list = new LinkedList<V>();

		for (int spot = 0; spot < size; spot++) {

			int clue = mark + spot;
			if (clue >= size) {
				clue -= size;
			}

			assert isValidRange(clue) : " clue=" + clue;

			if (isEmpty(clue)) {
				continue;
			}

			list.add(arrayGet(clue));

		}

		return list;

	}

	@Override
	public final Map<Integer, V> asMap() {

		final int size = length();
		final int head = head();
		final int mark = mark();

		final Map<Integer, V> map = new HashMap<Integer, V>();

		for (int spot = 0; spot < size; spot++) {

			int clue = mark + spot;
			if (clue >= size) {
				clue -= size;
			}

			assert isValidRange(clue) : " clue=" + clue;

			if (isEmpty(clue)) {
				continue;
			}

			map.put(head + spot, arrayGet(clue));

		}

		return map;

	}

}
