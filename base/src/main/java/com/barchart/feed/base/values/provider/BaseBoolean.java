/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.BooleanValue;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
abstract class BaseBoolean extends ValueFreezer<BooleanValue> implements
		BooleanValue {

	@Override
	public final int compareTo(final BooleanValue that) {

		if (this.asBoolean() == that.asBoolean()) {
			return 0;
		}

		return this.asBoolean() ? 1 : -1;

	}

	@Override
	public final int hashCode() {
		return this.asBoolean() ? 1 : 0;
	}

	@Override
	public final boolean equals(Object that) {
		if (that instanceof BooleanValue) {
			BooleanValue thatBool = (BooleanValue) that;
			return this.asBoolean() == thatBool.asBoolean();
		}
		return false;
	}

	@Override
	public final String toString() {
		return String.format("Boolean > " + asBoolean());
	}

	@Override
	public final boolean isNull() {
		return this == ValueConst.NULL_BOOLEAN;
	}

}
