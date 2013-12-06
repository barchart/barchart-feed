/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.cuvol.api;

import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.common.anno.NotMutable;

/** price & size ladder for cumulative volume */
@NotMutable
public interface MarketCuvol extends Value<MarketCuvol>, Cuvol {

	/** first non empty level in the price ladder */
	PriceValue priceFirst();

	/** price tick or price increment */
	PriceValue priceStep();

	/** array of sizes at the appropriate price level; */
	SizeValue[] entries();

}