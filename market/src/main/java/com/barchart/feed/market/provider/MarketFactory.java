package com.barchart.feed.market.provider;

import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.api.data.Market;
import com.barchart.missive.core.ObjectMapFactory;

public final class MarketFactory {
	
	private MarketFactory() {
		
	}
	
	public static Market newMarket(final InstrumentEntity inst) {
		
		final MarketBase market = ObjectMapFactory.build(MarketBase.class);
		
		market.setInstrument(inst);
		
		return market;
		
	}

}
