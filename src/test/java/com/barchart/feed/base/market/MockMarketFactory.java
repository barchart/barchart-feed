/**
 * 
 */
package com.barchart.feed.base.market;

import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.market.provider.MarketDo;
import com.barchart.feed.base.market.provider.MockMarket;

/**
 * @author g-litchfield
 * 
 */
public class MockMarketFactory implements MarketFactory {

	@Override
	public MarketDo newMarket() {
		return new MockMarket();
	}

}
