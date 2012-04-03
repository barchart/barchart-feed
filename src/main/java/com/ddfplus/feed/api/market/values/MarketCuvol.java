package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.Value;

/** price & size ladder for cumulative volume */
@NotMutable
public interface MarketCuvol extends Value<MarketCuvol> {

	/** first non empty level in the price ladder */
	PriceValue priceFirst();

	/** price tick or price increment */
	PriceValue priceStep();

	/** array of sizes at the appropriate price level; */
	SizeValue[] entries();

}
