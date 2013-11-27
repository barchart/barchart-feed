/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.TextValue;

/** represents long-based uuid */
// 24 bytes on 32 bit JVM
class DefTextId extends BaseText {

	private final long value;
	private final int hash;

	DefTextId(final long value) {
		this.value = value;
		this.hash = this.toString().hashCode();
	}

	@Override
	public int hashCode() {
		return hash;
	}

	public boolean equlas(final Object other) {
		if (other instanceof DefTextId) {
			final DefTextId that = (DefTextId) other;
			return this.value == that.value;
		}
		return super.equals(other);
	}

	@Override
	public TextValue toUpperCase() {
		return this;
	}

	@Override
	public TextValue toLowerCase() {
		return this;
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}

}
