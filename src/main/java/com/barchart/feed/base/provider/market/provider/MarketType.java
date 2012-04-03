/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

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
