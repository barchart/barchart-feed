/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import static com.barchart.feed.base.values.provider.ValueBuilder.newSize;

import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.util.common.anno.Mutable;
import com.barchart.util.common.anno.NotThreadSafe;

//16 bytes on 32 bit JVM
@Mutable
@NotThreadSafe
final class VarSize extends BaseSize {

	private volatile long value;

	VarSize(final long value) {
		this.value = value;
	}

	@Override
	public final long asLong() {
		return value;
	}

	@Override
	protected final SizeValue returnSize(final long value) {
		this.value = value;
		return this;
	}

	@Override
	public final SizeValue freeze() {
		return newSize(value);
	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

}
