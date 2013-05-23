/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

@NotMutable
public interface MarketEntry {

	/** price for a book level or a cuvol level */
	PriceValue priceValue();

	/** size for a book level or a cuvol level */
	SizeValue sizeValue();

}
