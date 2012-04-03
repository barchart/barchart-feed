package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.ddfplus.feed.api.market.values.MarketCuvol;
import com.ddfplus.feed.api.market.values.MarketCuvolEntry;

interface MarketDoCuvol extends MarketCuvol {

	void add(PriceValue price, SizeValue size);

	MarketCuvolEntry getLastEntry();

	void clear();

}
