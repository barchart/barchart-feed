/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.market;

import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.enums.MarketEvent;
import com.barchart.feed.base.api.market.enums.MarketField;
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

	/** specify value type that will be provided in the {@link #onMarketEvent} */
	@UsedOnce
	MarketField<V> bindField();

	/** specify list of events this taker will subscribe to */
	@UsedOnce
	MarketEvent[] bindEvents();

	/** specify list of markets this taker will subscribe to */
	@UsedOnce
	MarketInstrument[] bindInstruments();

	//

	/**
	 * event handler method that will receive subscribed events for subscribed
	 * markets; returned values are frozen and disconnected from live market
	 */
	@EventListener(MarketEvent.class)
	void onMarketEvent(MarketEvent event, MarketInstrument instrument,
			@NotMutable V value);

	//

}
