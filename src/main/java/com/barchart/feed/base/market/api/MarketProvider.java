/**
 * 
 */
package com.barchart.feed.base.market.api;



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
