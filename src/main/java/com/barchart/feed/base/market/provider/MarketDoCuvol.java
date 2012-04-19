/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import com.barchart.feed.base.market.values.MarketCuvol;
import com.barchart.feed.base.market.values.MarketCuvolEntry;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

interface MarketDoCuvol extends MarketCuvol {

	void add(PriceValue price, SizeValue size);

	MarketCuvolEntry getLastEntry();

	void clear();

}
