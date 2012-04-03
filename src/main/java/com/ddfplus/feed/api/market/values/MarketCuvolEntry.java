package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/** stand alone market cumulative volume value */
@NotMutable
public interface MarketCuvolEntry extends Value<MarketCuvolEntry>, MarketEntry {

	/** logical or relative index of this cuvol entry in the price ladder */
	int place();

}
