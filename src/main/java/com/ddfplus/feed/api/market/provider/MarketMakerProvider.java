package com.ddfplus.feed.api.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.anno.NotYetImplemented;
import com.barchart.util.anno.ThreadSafe;
import com.ddfplus.feed.api.market.MarketMaker;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.message.MarketMessage;

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

}
