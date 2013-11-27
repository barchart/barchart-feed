/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.util.anno.NotMutable;

// 16 bytes on 32 bit JVM
@NotMutable
final class DefPrice5 extends BasePrice {

	private final long mantissa;

	DefPrice5(final long mantissa) {
		this.mantissa = mantissa;
	}

	@Override
	public final long mantissa() {
		return mantissa;
	}

	@Override
	public final int exponent() {
		return -5;
	}

}
