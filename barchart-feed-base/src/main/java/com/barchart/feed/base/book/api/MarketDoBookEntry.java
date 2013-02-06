/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.util.anno.NotMutable;

@NotMutable
public interface MarketDoBookEntry extends MarketBookEntry {

	MarketBookAction act();

	BookLiquidityType type();

}
