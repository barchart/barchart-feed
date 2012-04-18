/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import com.barchart.feed.base.api.market.enums.MarketBookSide;
import com.barchart.feed.base.api.market.values.MarketBook;
import com.barchart.feed.base.api.market.values.MarketBookEntry;
import com.barchart.feed.base.provider.market.api.MarketDoBookEntry;
import com.barchart.util.values.api.TimeValue;

public interface MarketDoBook extends MarketBook {

	UniBookResult setEntry(MarketDoBookEntry entry);

	/** TODO elimintate: temp hack for ddf */
	UniBookResult setSnapshot(MarketDoBookEntry[] entries);

	void setTime(TimeValue time);

	void clear();

	MarketBookEntry last();

	MarketBookEntry top(MarketBookSide side);

}
