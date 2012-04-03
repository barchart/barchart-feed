package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.ThreadSafe;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.common.util.collections.FastEnumMap;

@ThreadSafe
class EventMap<T> extends FastEnumMap<MarketEvent, T> {

	private static final MarketEvent[] EVENTS = MarketEvent.values();

	EventMap() {
		super(EVENTS);
	}

}
