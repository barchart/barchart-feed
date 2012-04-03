package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotYetImplemented;

public enum MarketType implements MarketFactory {

	/* barchart ddf logic */
	DDF {
		@Override
		public MarketDo newMarket() {
			return new VarMarketDDF();
		}
	}, //

	/* generic fix/fast logic */
	FIX {
		@NotYetImplemented
		@Override
		public MarketDo newMarket() {
			return null;
		}
	}, //

	;

	@Override
	public abstract MarketDo newMarket();

}
