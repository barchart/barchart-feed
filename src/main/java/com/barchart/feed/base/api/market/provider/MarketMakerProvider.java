/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.market.provider;

import com.barchart.feed.base.api.market.MarketMaker;
import com.barchart.feed.base.api.market.enums.MarketField;
import com.barchart.feed.base.api.message.MarketMessage;
import com.barchart.feed.base.provider.market.api.MarketFactory;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.anno.NotYetImplemented;
import com.barchart.util.anno.ThreadSafe;

@ThreadSafe
public interface MarketMakerProvider<Message extends MarketMessage> extends
		MarketMaker {

	/**
	 * remove all markets and all takers
	 */
	void clearAll();

	//

	/**
	 * note: fires events from invocation thread
	 */
	void make(@NotMutable Message message);

	//

	@NotYetImplemented
	void copyTo(MarketMakerProvider<Message> maker, MarketField<?>... fields);

	//

	/**
	 * add listener that will receive notifications when any taker is registered
	 * or un-registered
	 */
	void add(MarketRegListener listener);

	/** */
	void remove(MarketRegListener listener);

	/**
	 * notify all registered market registration listeners for all registered
	 * markets
	 */
	void notifyRegListeners();

	//

	void appendMarketProvider(MarketFactory marketFactory);

}
