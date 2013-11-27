/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import static com.barchart.feed.base.values.provider.ValueBuilder.newText;

import com.barchart.feed.base.util.ASCII;
import com.barchart.feed.base.values.api.TextValue;

//24+ bytes on 32 bit JVM
final class DefTextByteArray extends BaseText {

	private final byte[] array;

	private int hash;

	DefTextByteArray(final byte[] array) {
		assert array != null;
		// detach from source
		this.array = array.clone();
	}

	DefTextByteArray(final String string) {
		assert string != null;
		assert ValueBuilder.isPureAscii(string);
		this.array = string.getBytes(ASCII.ASCII_CHARSET);
	}

	// same as String.hashCode()
	@Override
	public final int hashCode() {
		int h = hash;
		if (h == 0) {
			final byte[] array = this.array;
			final int size = array.length;
			for (int k = 0; k < size; k++) {
				h = 31 * h + array[k];
			}
			hash = h;
		}
		return h;
	}

	@Override
	public final String toString() {
		return new String(array, ASCII.ASCII_CHARSET);
	}

	@Override
	public final char charAt(final int index) {
		return (char) (array[index] & 0xFF);
	}

	@Override
	public final int length() {
		return array.length;
	}

	@Override
	public final CharSequence subSequence(final int start, final int end) {

		assert 0 <= start & start <= array.length;
		assert 0 <= end & end <= array.length;
		assert start <= end;

		final int size = end - start;
		final byte[] subArray = new byte[size];
		System.arraycopy(array, start, subArray, 0, size);

		return newText(subArray);

	}

	@Override
	public TextValue toUpperCase() {
		final byte[] array = this.array;
		final int size = array.length;
		final byte[] upper = new byte[size];
		int count = 0;
		for (int k = 0; k < size; k++) {
			final byte ascii = array[k];
			if (ASCII._a_ <= ascii && ascii <= ASCII._z_) {
				upper[k] = (byte) (ascii - 32);
				count++;
			} else {
				upper[k] = ascii;
			}
		}
		if (count == 0) {
			return this;
		} else {
			return new DefTextByteArray(upper);
		}
	}

	@Override
	public TextValue toLowerCase() {
		final byte[] array = this.array;
		final int size = array.length;
		final byte[] lower = new byte[size];
		int count = 0;
		for (int k = 0; k < size; k++) {
			final byte ascii = array[k];
			if (ASCII._A_ <= ascii && ascii <= ASCII._Z_) {
				lower[k] = (byte) (ascii + 32);
				count++;
			} else {
				lower[k] = ascii;
			}
		}
		if (count == 0) {
			return this;
		} else {
			return new DefTextByteArray(lower);
		}
	}

}
