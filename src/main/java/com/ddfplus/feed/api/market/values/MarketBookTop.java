package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketBookSide;

@NotMutable
public interface MarketBookTop extends Value<MarketBookTop> {

	/** last time top update on either side */
	TimeValue time();

	/** @return price, size, place for a side */
	MarketBookEntry side(MarketBookSide side);

}
