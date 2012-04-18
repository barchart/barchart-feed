/**
 * 
 */
package com.barchart.feed.base.provider.market.api;

import com.barchart.feed.base.provider.market.provider.MarketDo;

/**
 * A hamfisted way of injecting the application specific implementation MarketDo
 * type into the MarketMakerProvider.
 * 
 * @author g-litchfield
 * 
 */
public interface MarketProvider {

	public MarketFactory makeFactory(MarketDo market);

}
