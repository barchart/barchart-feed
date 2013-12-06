/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.util.common.anno.NotMutable;
import com.barchart.util.common.math.MathExtra;

@NotMutable
abstract class BaseSize extends ValueFreezer<SizeValue> implements SizeValue {

	//

	protected abstract SizeValue returnSize(long value);

	@Override
	public abstract long asLong();

	//

	@Deprecated
	@Override
	public final int asInt() {
		return MathExtra.castLongToInt(asLong());
	}

	@Override
	public final int compareTo(final SizeValue that) {
		final long v1 = this.asLong();
		final long v2 = that.asLong();
		return v1 < v2 ? -1 : (v1 == v2 ? 0 : 1);
	}

	@Override
	public final int hashCode() {
		final long value = asLong();
		return (int) (value ^ (value >>> 32));
	}

	@Override
	public final boolean equals(Object thatSize) {
		if (thatSize instanceof SizeValue) {
			SizeValue that = (SizeValue) thatSize;
			return this.asLong() == that.asLong();
		}
		return false;
	}

	@Override
	public final String toString() {
		return String.format("Size > %9d", asLong()); // 16
	}

	@Override
	public final boolean isNull() {
		return this == ValueConst.NULL_SIZE;
	}

	@Override
	public final SizeValue add(final SizeValue that) throws ArithmeticException {
		return returnSize(MathExtra.longAdd(this.asLong(), that.asLong()));
	}

	@Override
	public final SizeValue sub(final SizeValue that) throws ArithmeticException {
		return returnSize(MathExtra.longSub(this.asLong(), that.asLong()));
	}

	@Override
	public final SizeValue mult(final long factor) throws ArithmeticException {
		return returnSize(MathExtra.longMult(this.asLong(), factor));
	}

	@Override
	public final SizeValue div(final long factor) throws ArithmeticException {
		return returnSize(this.asLong() / factor);
	}

	@Override
	public final long count(final SizeValue that) throws ArithmeticException {
		return (this.asLong() / that.asLong());
	}

	@Override
	public final boolean isZero() {
		return asLong() == 0L;
	}

}
