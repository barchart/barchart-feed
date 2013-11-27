/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import static com.barchart.feed.base.values.provider.ValueBuilder.*;

import com.barchart.feed.base.values.api.TextValue;

abstract class BaseText extends ValueFreezer<TextValue> implements TextValue {

	@Override
	public abstract String toString();

	@Override
	public final int compareTo(final CharSequence that) {
		final int s1 = this.length();
		final int s2 = that.length();
		final int size = Math.min(s1, s2);
		for (int k = 1; k < size; k++) {
			final int c1 = this.charAt(k);
			final int c2 = that.charAt(k);
			if (c1 == c2) {
				continue;
			} else {
				return c1 - c2;
			}
		}
		return s1 - s2;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public final boolean equals(final Object thatText) {
		if (thatText instanceof CharSequence) {
			final CharSequence that = (CharSequence) thatText;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public char charAt(final int index) {
		return toString().charAt(index);
	}

	@Override
	public int length() {
		return toString().length();
	}

	@Override
	public final TextValue concat(final CharSequence that) {

		final int sizeThis = this.length();
		final int sizeThat = that.length();
		final int size = sizeThis + sizeThat;

		if (isPureAscii(this) && isPureAscii(that)) {
			final byte[] array = new byte[size];
			int index = 0;
			for (int k = 0; k < sizeThis; k++) {
				array[index++] = (byte) this.charAt(k);
			}
			for (int k = 0; k < sizeThat; k++) {
				array[index++] = (byte) that.charAt(k);
			}
			return newText(array);
		} else {
			final char[] array = new char[size];
			int index = 0;
			for (int k = 0; k < sizeThis; k++) {
				array[index++] = this.charAt(k);
			}
			for (int k = 0; k < sizeThat; k++) {
				array[index++] = that.charAt(k);
			}
			return newText(array);
		}

	}

	@Override
	public CharSequence subSequence(final int start, final int end) {
		return toString().substring(start, end);
	}

}
