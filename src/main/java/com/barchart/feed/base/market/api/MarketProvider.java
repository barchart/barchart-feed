/**
 * 
 */
package com.barchart.feed.base.market.api;

import com.barchart.feed.base.market.provider.MarketDo;


/**
 * A hamfisted way of injecting the application specific implementation MarketDo
 * type into the MarketMakerProvider.
 * 
 * @author g-litchfield
 * 
 */
public interface MarketProvider {

	public MarketFactory makeFactory(final Class<? extends MarketDo> clazz);

}
