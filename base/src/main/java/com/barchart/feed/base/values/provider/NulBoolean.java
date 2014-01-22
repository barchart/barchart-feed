/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.util.common.anno.NotMutable;

// 8 bytes on 32 bit JVM
@NotMutable
final class NulBoolean extends BaseBoolean {

	@Override
	public Boolean asBoolean() {
		return Boolean.FALSE;
	}

}
