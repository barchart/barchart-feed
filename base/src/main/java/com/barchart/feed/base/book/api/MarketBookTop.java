/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;

@NotMutable
public interface MarketBookTop extends Value<MarketBookTop>, TopOfBook {

	/** last time top update on either side */
	TimeValue time();

	/** @return price, size, place for a side */
	MarketBookEntry side(MarketSide side);

}
