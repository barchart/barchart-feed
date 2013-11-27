/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import static com.barchart.feed.base.values.provider.ValueBuilder.isPureAscii;

import com.barchart.feed.base.util.ASCII;
import com.barchart.feed.base.values.api.TextValue;

// ASCII, NUL terminated, up to 8 chars 

//16 bytes on 32 bit JVM
final class DefTextLong extends BaseText {

	static final byte NUL = 0;

	private final long value;

	final static boolean isValid(final byte[] array) {
		if (array == null) {
			throw new NullPointerException();
		}
		if (array.length > 8) {
			throw new ArrayIndexOutOfBoundsException();
		}
		for (final byte b : array) {
			if (b == NUL) {
				throw new IllegalArgumentException();
			}
		}
		return true;
	}

	DefTextLong(final long value) {
		this.value = value;
	}

	/** store array{1,2,3} as 3-2-1 */
	DefTextLong(final byte[] array) {

		assert isValid(array);

		long value = 0L;

		for (int k = array.length - 1; k >= 0; k--) {
			final byte b = array[k];
			value |= b;
			if (b == NUL) {
				break;
			}
			if (k == 0) {
				break;
			}
			value <<= 8;
		}

		this.value = value;

	}

	DefTextLong(final String string) {

		this(string.getBytes(ASCII.ASCII_CHARSET));

		assert isPureAscii(string);

	}

	// same as String.hashCode()
	@Override
	public final int hashCode() {
		int h = 0;
		long value = this.value;
		for (int k = 0; k < 8; k++) {
			final char c = (char) (value & 0xFFL);
			if (c == NUL) {
				break;
			}
			h = 31 * h + c;
			value >>>= 8;
		}
		return h;
	}

	@Override
	public final String toString() {
		final char[] array = new char[8];
		long value = this.value;
		int k = 0;
		for (; k < 8; k++) {
			final char c = (char) (value & 0xFFL);
			if (c == NUL) {
				break;
			}
			array[k] = c;
			value >>>= 8;
		}
		return new String(array, 0, k);
	}

	@Override
	public final char charAt(final int index) {
		return (char) ((value >>> (index << 3)) & 0xFFL);
	}

	@Override
	public final int length() {
		long value = this.value;
		int k = 0;
		while (value != 0L) {
			value >>>= 8;
			k++;
		}
		return k;
	}

	@Override
	public final CharSequence subSequence(final int start, final int end) {

		assert 0 <= start & start <= 8;
		assert 0 <= end & end <= 8;
		assert start <= end;

		long value = this.value;
		value &= -1L >>> ((8 - end) << 3);
		value >>>= (start << 3);

		return new DefTextLong(value);

	}

	final static long LO = 0xFFL;
	final static long HI = 0xFF00000000000000L;

	@Override
	public TextValue toUpperCase() {

		long value = this.value;
		long upper = 0L;

		int count = 0;
		while (value != 0L) {
			long ascii = ((value & HI) >>> 56);
			value <<= 8;
			if (ascii == 0L) {
				continue;
			}
			upper <<= 8;
			if (ASCII._a_ <= ascii && ascii <= ASCII._z_) {
				ascii -= 32;
				count++;
			}
			upper |= ascii;
		}

		if (count == 0) {
			return this;
		} else {
			return new DefTextLong(upper);
		}

	}

	@Override
	public TextValue toLowerCase() {

		long value = this.value;
		long lower = 0L;

		int count = 0;
		while (value != 0L) {
			long ascii = ((value & HI) >>> 56);
			value <<= 8;
			if (ascii == 0L) {
				continue;
			}
			lower <<= 8;
			if (ASCII._A_ <= ascii && ascii <= ASCII._Z_) {
				ascii += 32;
				count++;
			}
			lower |= ascii;
		}

		if (count == 0) {
			return this;
		} else {
			return new DefTextLong(lower);
		}

	}
}
