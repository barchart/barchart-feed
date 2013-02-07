/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.cuvol.map.api;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.Value;

@NotMutable
public interface MarketCuvolMap extends Value<MarketCuvolMap> {

	/** returns the cumulative volume for the price level */
	SizeValue getCuvol(PriceValue price);
	
	/** first non empty level in the price ladder */
	PriceValue priceFirst();

	/** price tick or price increment */
	PriceValue priceStep();

	/** array of sizes at the appropriate price level; */
	SizeValue[] entries();
	
}
