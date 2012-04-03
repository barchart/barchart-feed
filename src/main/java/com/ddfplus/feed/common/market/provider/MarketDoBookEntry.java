package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.ddfplus.feed.api.market.enums.MarketBookAction;
import com.ddfplus.feed.api.market.enums.MarketBookType;
import com.ddfplus.feed.api.market.values.MarketBookEntry;

@NotMutable
public interface MarketDoBookEntry extends MarketBookEntry {

	MarketBookAction act();

	MarketBookType type();

}
