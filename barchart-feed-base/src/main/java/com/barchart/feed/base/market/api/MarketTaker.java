/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.anno.EventListener;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.anno.UsedOnce;
import com.barchart.util.values.api.Value;

/**
 * NOTE: bind happens only once at taker registration
 * 
 */
@NotMutable
public interface MarketTaker<V extends Value<V>> {

	/** Specify value type that will be provided in the {@link #onMarketEvent} */
	@UsedOnce
	MarketField<V> bindField();

	/** Specify list of events this taker will subscribe to */
	@UsedOnce
	MarketEvent[] bindEvents();

	/** Specify list of markets this taker will subscribe to */
	@UsedOnce
	Instrument[] bindInstruments();

	//

	/**
	 * Event handler method that will receive subscribed events for subscribed
	 * markets; returned values are frozen and disconnected from live market
	 */
	@EventListener(MarketEvent.class)
	void onMarketEvent(MarketEvent event, Instrument instrument,
			@NotMutable V value);

	//

}
