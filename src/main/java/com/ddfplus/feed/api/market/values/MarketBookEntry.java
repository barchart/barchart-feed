package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketBookSide;

@NotMutable
public interface MarketBookEntry extends Value<MarketBookEntry>, MarketEntry {

	/** bid vs ask */
	MarketBookSide side();

	/**
	 * logical position in the bid or ask side;
	 * 
	 * starts with {@link MarketBook#ENTRY_TOP}
	 * */
	int place();

}
