/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.util.values.api.TimeValue;

public interface MarketDoBook extends MarketBook {

	UniBookResult setEntry(MarketDoBookEntry entry);

	/** TODO elimintate: temp hack for ddf */
	UniBookResult setSnapshot(MarketDoBookEntry[] entries);

	void setTime(TimeValue time);

	void clear();

	MarketBookEntry last();

	MarketBookEntry top(MarketSide side);

}
