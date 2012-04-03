package com.ddfplus.feed.api.market.provider;

import java.util.Set;

import com.barchart.util.anno.EventListener;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.MarketMaker;
import com.ddfplus.feed.api.market.MarketTaker;
import com.ddfplus.feed.api.market.enums.MarketEvent;

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
