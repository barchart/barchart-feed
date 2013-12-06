/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import java.util.Map;
import java.util.Set;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.util.common.anno.EventListener;

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
			Map<Instrument, Set<MarketEvent>> instMap);

}
