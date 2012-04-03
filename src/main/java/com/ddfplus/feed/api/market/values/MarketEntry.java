package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

@NotMutable
public interface MarketEntry {

	/** price for a book level or a cuvol level */
	PriceValue price();

	/** size for a book level or a cuvol level */
	SizeValue size();

}
