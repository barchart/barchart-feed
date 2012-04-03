package com.ddfplus.feed.api.market.values;

import java.util.Set;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketStateEntry;

/**
 */
@NotMutable
public interface MarketState extends Value<MarketState>, Set<MarketStateEntry> {

}
