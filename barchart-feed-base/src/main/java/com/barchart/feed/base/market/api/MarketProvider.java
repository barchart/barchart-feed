/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
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
