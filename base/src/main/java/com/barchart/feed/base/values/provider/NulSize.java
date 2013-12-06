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

// 8 bytes on 32 bit JVM
@NotMutable
final class NulSize extends BaseSize {

	@Override
	public final long asLong() {
		return 0L;
	}

	@Override
	protected final SizeValue returnSize(long value) {
		return ValueBuilder.newSize(value);
	}

}
