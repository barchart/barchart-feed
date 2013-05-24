package com.barchart.feed.market.provider;

import com.barchart.feed.api.consumer.data.Market;
import com.barchart.feed.api.framework.data.InstrumentEntity;
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
