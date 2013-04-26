package com.barchart.feed.market.provider;

import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.Market;
import com.barchart.missive.core.ObjectMapFactory;

public final class MarketFactory {
	
	private MarketFactory() {
		
	}
	
	public static Market newMarket(final Instrument inst) {
		
		final MarketBase market = ObjectMapFactory.build(MarketBase.class);
		
		market.setInstrument(inst);
		
		return market;
		
	}

}
