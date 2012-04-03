package com.ddfplus.feed.common.market.provider;

import java.util.Collections;
import java.util.Set;

import com.barchart.util.anno.NotThreadSafe;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.common.util.collections.FastEnumSet;

@NotThreadSafe
class EventSet extends FastEnumSet<MarketEvent> {

	private static final MarketEvent[] EVENTS = MarketEvent.values();

	static final Set<MarketEvent> EMPTY = Collections
			.unmodifiableSet(new EventSet());

	EventSet() {
		super(EVENTS);
	}

	EventSet(final MarketEvent... events) {
		super(EVENTS, events);
	}

}
