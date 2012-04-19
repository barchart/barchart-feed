/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.collections.FastEnumSet;

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
