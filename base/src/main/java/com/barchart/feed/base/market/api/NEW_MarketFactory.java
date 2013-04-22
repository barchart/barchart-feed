package com.barchart.feed.base.market.api;

import com.barchart.feed.api.inst.Instrument;

public interface NEW_MarketFactory {

	NEW_MarketDo newMarket(Instrument instrument);
	
}
