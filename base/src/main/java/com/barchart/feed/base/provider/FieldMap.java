/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.collections.FastEnumMap;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.ThreadSafe;

@ThreadSafe
class FieldMap<T> extends FastEnumMap<MarketField<?>, T> {

	private static final MarketField<?>[] FIELDS = MarketField.values();

	FieldMap() {
		super(FIELDS);
	}

}
