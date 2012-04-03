package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.TimeValue;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.values.MarketBook;
import com.ddfplus.feed.api.market.values.MarketBookEntry;

interface MarketDoBook extends MarketBook {

	UniBookResult setEntry(MarketDoBookEntry entry);

	/** TODO elimintate: temp hack for ddf */
	UniBookResult setSnapshot(MarketDoBookEntry[] entries);

	void setTime(TimeValue time);

	void clear();

	MarketBookEntry last();

	MarketBookEntry top(MarketBookSide side);

}
