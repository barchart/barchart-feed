/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.collections.FastEnumSet;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.NotThreadSafe;

@NotThreadSafe
class FieldSet extends FastEnumSet<MarketField<?>> {

	private static final MarketField<?>[] FIELDS = MarketField.values();

	FieldSet() {
		super(FIELDS);
	}

}
