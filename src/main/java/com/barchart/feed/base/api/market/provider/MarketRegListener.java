/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.market.provider;

import java.util.Set;

import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.MarketMaker;
import com.barchart.feed.base.api.market.MarketTaker;
import com.barchart.feed.base.api.market.enums.MarketEvent;
import com.barchart.util.anno.EventListener;

public interface MarketRegListener {

	/**
	 * called from {@link MarketMaker#register}/{@link MarketMaker#unregister}
	 * invoker thread;
	 * 
	 * @param events
	 *            market interest events, after the the change is applied
	 */
	@EventListener(MarketTaker.class)
	void onRegistrationChange(//
			MarketInstrument instrument, Set<MarketEvent> events);

}
