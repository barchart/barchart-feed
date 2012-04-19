/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import com.barchart.feed.base.market.enums.MarketBookAction;
import com.barchart.feed.base.market.enums.MarketBookType;
import com.barchart.feed.base.market.values.MarketBookEntry;
import com.barchart.util.anno.NotMutable;

@NotMutable
public interface MarketDoBookEntry extends MarketBookEntry {

	MarketBookAction act();

	MarketBookType type();

}
