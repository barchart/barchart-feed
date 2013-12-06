/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.Collections;
import java.util.List;

import com.barchart.feed.base.collections.FastArrayList;
import com.barchart.util.common.anno.NotThreadSafe;

@NotThreadSafe
final class RegTakerList extends FastArrayList<RegTaker<?>> {

	static final List<RegTaker<?>> EMPTY = Collections
			.unmodifiableList(new RegTakerList());

}
