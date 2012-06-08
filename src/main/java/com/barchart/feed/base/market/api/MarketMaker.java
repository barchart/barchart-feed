/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.util.values.api.Value;

public interface MarketMaker {

	boolean isRegistered(MarketTaker<?> taker);

	/**
	 * add taker; do instrument registration; fires {@link MarketRegListener}
	 * events from invocation thread;
	 */
	<V extends Value<V>> boolean register(MarketTaker<V> taker);

	/**
	 * remove taker; do instrument un-registration; fires
	 * {@link MarketRegListener} events from invocation thread;
	 */
	<V extends Value<V>> boolean unregister(MarketTaker<V> taker);

	//

	int marketCount();

	boolean isRegistered(MarketInstrument instrument);

	//

	/**
	 * obtain market field value snapshot; returned values are frozen and
	 * disconnected from live market;
	 * 
	 * @return NULL_VALUE if market is not present
	 */
	<S extends MarketInstrument, V extends Value<V>> V //
			take(S instrument, MarketField<V> field);

}
